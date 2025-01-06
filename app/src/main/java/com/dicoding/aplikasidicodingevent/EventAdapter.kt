package com.dicoding.aplikasidicodingevent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aplikasidicodingevent.ui.favorite.FavoriteEvent

class EventAdapter(
    private val context: Context,
    private val clickListener: (String) -> Unit
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    private var favoriteEvents: MutableList<FavoriteEvent> = mutableListOf()
    private var apiEvents: MutableList<ListEventsItem> = mutableListOf()
    private var isFromApi: Boolean = false

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.item_Image)
        private val textView: TextView = itemView.findViewById(R.id.item_Text)

        fun bindFavorite(event: FavoriteEvent) {
            Glide.with(context)
                .load(event.mediaCover)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageView)

            textView.text = event.name

            itemView.setOnClickListener {
                clickListener(event.id.toString())
            }
        }

        fun bindApi(event: ListEventsItem) {
            Glide.with(context)
                .load(event.mediaCover)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageView)

            textView.text = event.name

            itemView.setOnClickListener {
                clickListener(event.id.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isFromApi) {
            holder.bindApi(apiEvents[position])
        } else {
            holder.bindFavorite(favoriteEvents[position])
        }
    }

    override fun getItemCount(): Int = if (isFromApi) apiEvents.size else favoriteEvents.size

    fun submitFavoriteList(newList: List<FavoriteEvent>?) {
        isFromApi = false
        favoriteEvents.clear()
        if (newList != null) {
            favoriteEvents.addAll(newList)
        }
        notifyDataSetChanged()
    }

    fun submitApiList(newList: List<ListEventsItem>?) {
        isFromApi = true
        apiEvents.clear()
        if (newList != null) {
            apiEvents.addAll(newList)
        }
        notifyDataSetChanged()
    }
}