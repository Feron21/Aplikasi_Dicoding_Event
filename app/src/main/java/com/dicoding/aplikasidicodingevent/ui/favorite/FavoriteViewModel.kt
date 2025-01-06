package com.dicoding.aplikasidicodingevent.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: FavoriteRepository) : ViewModel() {

    fun getFavoriteEventById(id: Int): LiveData<FavoriteEvent> = repository.getFavoriteEventById(id.toString())

    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>> = repository.getAllFavoriteEvents()

    fun toggleFavorite(event: FavoriteEvent) = viewModelScope.launch {
        val isFavorite = repository.getFavoriteEventById(event.id.toString()).value != null
        if (isFavorite) {
            repository.deleteFavorite(event)
        } else {
            repository.insertFavorite(event)
        }
    }
}

