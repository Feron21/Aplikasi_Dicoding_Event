// DetailActivity.kt
package com.dicoding.aplikasidicodingevent.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.aplikasidicodingevent.R
import com.dicoding.aplikasidicodingevent.retrofit.ApiConfig
import com.dicoding.aplikasidicodingevent.ui.favorite.AppDatabase
import com.dicoding.aplikasidicodingevent.ui.favorite.FavoriteEvent
import com.dicoding.aplikasidicodingevent.ui.favorite.FavoriteRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var fabFavorite: FloatingActionButton
    private lateinit var favoriteRepository: FavoriteRepository
    private var isFavorite: Boolean = false
    private lateinit var eventId: String
    private lateinit var eventName: String
    private var eventMediaCover: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        progressBar = findViewById(R.id.progressBar)
        fabFavorite = findViewById(R.id.fab_favorite)

        // Inisialisasi FavoriteRepository
        val database = AppDatabase.getDatabase(this)
        favoriteRepository = FavoriteRepository(database.favoriteDao())

        eventId = intent.getStringExtra("EVENT_ID") ?: return

        // Load status favorite dari database
        loadFavoriteStatus()
        loadEventData(eventId)

        fabFavorite.setOnClickListener {
            toggleFavoriteStatus()
        }
    }

    private fun loadEventData(eventId: String) {
        showLoadingIndicator()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig.getApiService().getDetailEvent(eventId).execute()
                val event = response.body()?.event

                withContext(Dispatchers.Main) {
                    if (event != null) {
                        eventName = event.name.toString()
                        eventMediaCover = event.mediaCover
                        displayEventDetail(event)
                    } else {
                        Log.e("DetailActivity", "Event data is null")
                    }
                    hideLoadingIndicator()
                }
            } catch (e: Exception) {
                Log.e("DetailActivity", "Error fetching event: ${e.message}")
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                }
            }
        }
    }

    private fun displayEventDetail(event: Event) {
        findViewById<ImageView>(R.id.detailImage).let {
            Glide.with(this).load(event.mediaCover).into(it)
        }
        findViewById<TextView>(R.id.detailTitle).text = event.name
        findViewById<TextView>(R.id.detailOwner).text = event.ownerName
        findViewById<TextView>(R.id.detailBeginTime).text = event.beginTime
        val quotaLeft = event.quota?.minus(event.registrants ?: 0)
        findViewById<TextView>(R.id.detailQuota).text = "$quotaLeft / ${event.quota}"
        findViewById<TextView>(R.id.detailDescription).text = HtmlCompat.fromHtml(
            event.description ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        findViewById<Button>(R.id.buttonDetail).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(event.link)
            }
            startActivity(intent)
        }
    }

    private fun toggleFavoriteStatus() {
        isFavorite = !isFavorite
        updateFavoriteIcon()

        // Buat objek FavoriteEvent
        val event = FavoriteEvent(id = eventId.toInt(), name = eventName, mediaCover = eventMediaCover)

        // Tambah atau hapus dari Room berdasarkan status favorite
        CoroutineScope(Dispatchers.IO).launch {
            if (isFavorite) {
                favoriteRepository.insertFavorite(event)
            } else {
                favoriteRepository.deleteFavorite(event)
            }
        }
    }

    private fun loadFavoriteStatus() {
        // Mengambil status favorite dari database
        favoriteRepository.getFavoriteEventById(eventId.toInt().toString()).observe(this) { favorite ->
            isFavorite = favorite != null
            updateFavoriteIcon()
        }
    }

    private fun updateFavoriteIcon() {
        val icon = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        fabFavorite.setImageResource(icon)
    }

    private fun showLoadingIndicator() {
        progressBar.visibility = View.VISIBLE
        findViewById<Button>(R.id.buttonDetail).visibility = View.INVISIBLE // Hide buttonDetail
        fabFavorite.visibility = View.INVISIBLE // Hide fabFavorite
    }

    private fun hideLoadingIndicator() {
        progressBar.visibility = View.GONE
        findViewById<Button>(R.id.buttonDetail).visibility = View.VISIBLE // Show buttonDetail
        fabFavorite.visibility = View.VISIBLE // Show fabFavorite
    }
}
