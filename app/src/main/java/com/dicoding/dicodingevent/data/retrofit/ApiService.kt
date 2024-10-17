package com.dicoding.dicodingevent.data.retrofit

import com.dicoding.dicodingevent.data.response.EventDetailResponse
import com.dicoding.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("events")
        fun getAllActiveEvent(
            @Query("active") active: Int = 1
        ): Call<EventResponse>

     @GET("events")
        fun getAllFinishedEvent(
            @Query("active") active: Int = 0
        ): Call<EventResponse>

    @GET("events")
        fun searchEvent(
            @Query("q") keyword: String,
            @Query("active") active: Int = -1
        ): Call<EventResponse>

    @GET("events/{id}")
        fun getDetailEvent(
            @Path("id") id: Int
        ): Call<EventDetailResponse>
}
