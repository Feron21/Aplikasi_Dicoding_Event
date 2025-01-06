package com.dicoding.aplikasidicodingevent.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.aplikasidicodingevent.EventAdapter
import com.dicoding.aplikasidicodingevent.MainActivity
import com.dicoding.aplikasidicodingevent.databinding.FragmentFavoriteBinding
import com.dicoding.aplikasidicodingevent.detail.DetailActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        observeFavorites()
        setupRecyclerViewScrollListener()
    }

    private fun setupViewModel() {
        val database = AppDatabase.getDatabase(requireContext())
        val repository = FavoriteRepository(database.favoriteDao())
        val factory = FavoriteViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter(
            context = requireContext(),
            clickListener = { eventId ->
                navigateToDetail(eventId)
            }
        )
        binding.recyclerViewFavorite.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoriteFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewScrollListener() {
        binding.recyclerViewFavorite.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val activity = activity as? MainActivity
                if (dy > 0) {
                    activity?.hideBottomNavigation()
                } else if (dy < 0) {
                    activity?.showBottomNavigation()
                }
            }
        })
    }

    private fun observeFavorites() {
        viewModel.getAllFavoriteEvents().observe(viewLifecycleOwner) { favorites ->
            if (favorites.isEmpty()) {
                showEmptyState()
            } else {
                showFavoriteList()
                adapter.submitFavoriteList(favorites)
            }
        }
    }

    private fun showEmptyState() {
        binding.apply {
            recyclerViewFavorite.visibility = View.GONE
            emptyStateLayout.visibility = View.VISIBLE
        }
    }

    private fun showFavoriteList() {
        binding.apply {
            recyclerViewFavorite.visibility = View.VISIBLE
            emptyStateLayout.visibility = View.GONE
        }
    }

    private fun navigateToDetail(eventId: String) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("EVENT_ID", eventId)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
