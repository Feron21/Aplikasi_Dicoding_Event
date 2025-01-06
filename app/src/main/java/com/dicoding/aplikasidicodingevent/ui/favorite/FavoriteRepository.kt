package com.dicoding.aplikasidicodingevent.ui.favorite

import androidx.lifecycle.LiveData

class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent> = favoriteDao.getFavoriteEventById(id)

    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>> = favoriteDao.getAllFavoriteEvents()

     fun insertFavorite(event: FavoriteEvent) = favoriteDao.insertFavorite(event)

     fun deleteFavorite(event: FavoriteEvent) = favoriteDao.deleteFavorite(event)
}
