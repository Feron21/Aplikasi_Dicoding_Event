package com.dicoding.aplikasidicodingevent.ui.home

import com.dicoding.aplikasidicodingevent.EventAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.aplikasidicodingevent.detail.DetailActivity
import com.dicoding.aplikasidicodingevent.databinding.FragmentHomeBinding
import com.dicoding.aplikasidicodingevent.retrofit.ApiConfig
import com.dicoding.aplikasidicodingevent.EventResponse
import com.dicoding.aplikasidicodingevent.ui.SpaceItemDecoration
import com.dicoding.aplikasidicodingevent.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchEventData()
        setupRecyclerViewScrollListener()
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter(
            context = requireContext(),
            clickListener = { eventId ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("EVENT_ID", eventId)
                startActivity(intent)
            }
        )

        binding.recycleApi.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@HomeFragment.adapter
            addItemDecoration(SpaceItemDecoration(16)) // Adds 16dp space at the bottom
        }
    }

    private fun setupRecyclerViewScrollListener() {
        binding.recycleApi.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val activity = activity as? MainActivity
                if (dy > 0) {
                    // User scrolled down, hide bottom navigation
                    activity?.hideBottomNavigation()
                } else if (dy < 0) {
                    // User scrolled up, show bottom navigation
                    activity?.showBottomNavigation()
                }
            }
        })
    }

    private fun fetchEventData() {
        showLoading(true)
        val apiService = ApiConfig.getApiService()
        apiService.getActiveEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val nonNullEvents = responseBody.listEvents
                        adapter.submitApiList(nonNullEvents)
                    } else {
                        Log.e("HomeFragment", "onFailure: Response body is null")
                    }
                } else {
                    Log.e("HomeFragment", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoading(false)
                Log.e("HomeFragment", "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        _binding?.let { binding ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
