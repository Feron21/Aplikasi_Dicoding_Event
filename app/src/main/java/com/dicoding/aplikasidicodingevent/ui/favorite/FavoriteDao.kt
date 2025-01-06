package com.dicoding.aplikasidicodingevent.ui.favorite

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// FavoriteDao.kt
@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertFavorite(event: FavoriteEvent)

    @Delete
     fun deleteFavorite(event: FavoriteEvent)

    @Query("SELECT * FROM FavoriteEvent WHERE id = :id")
     fun getFavoriteEventById(id: String): LiveData<FavoriteEvent>

    @Query("SELECT * FROM FavoriteEvent")
     fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>>
}

