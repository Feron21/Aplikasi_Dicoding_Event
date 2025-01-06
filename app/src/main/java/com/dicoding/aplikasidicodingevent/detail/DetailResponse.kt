package com.dicoding.aplikasidicodingevent.detail
import com.google.gson.annotations.SerializedName
data class DetailResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("event")
    val event: Event? = null
)