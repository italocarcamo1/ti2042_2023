package com.example.myfirebaseexample.api

import com.example.myfirebaseexample.api.response.PostResponse
import com.example.myfirebaseexample.api.response.BarberiaResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FirebaseApi {
    @GET("barberia.json")
    fun getBarberias(): Call<MutableMap<String, BarberiaResponse>>

    @GET("barberia/{id}.json")
    fun getBarberia(
        @Path("id") id: String
    ): Call<BarberiaResponse>

    @POST("barberia.json")
    fun setBarberia(
        @Body() body: BarberiaResponse
    ): Call<PostResponse>
}