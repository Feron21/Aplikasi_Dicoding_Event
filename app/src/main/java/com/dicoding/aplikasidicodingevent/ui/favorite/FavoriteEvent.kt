package com.dicoding.aplikasidicodingevent.ui.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Entity(tableName = "FavoriteEvent")
@Parcelize
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0, // Mengubah id menjadi Int

    @ColumnInfo(name = "name")
    val name: String, // Menambahkan @ColumnInfo

    @ColumnInfo(name = "media_cover")
    val mediaCover: String? = null // Menambahkan @ColumnInfo
) : Parcelable