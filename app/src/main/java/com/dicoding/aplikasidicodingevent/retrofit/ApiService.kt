package com.dicoding.aplikasidicodingevent.retrofit

import com.dicoding.aplikasidicodingevent.detail.DetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.dicoding.aplikasidicodingevent.EventResponse
import retrofit2.http.Path

interface ApiService {
    @GET("events")
    fun getActiveEvents(
        @Query("active") active: Int = 1): Call<EventResponse>

    @GET("events")
    fun getFinishEvents(
        @Query("active") active: Int = 0): Call<EventResponse>
    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: String): Call<DetailResponse>
}
