package com.example.myfirebaseexample.api

import com.example.myfirebaseexample.api.response.PostResponse
import com.example.myfirebaseexample.api.response.BarberiaResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body

class FirebaseApiAdapter {
    private var URL_BASE = "https://my-app-a534f-default-rtdb.firebaseio.com/"
    private val firebaseApi = Retrofit.Builder()
        .baseUrl(URL_BASE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getBarberias(): MutableMap<String, BarberiaResponse>? {
        val call = firebaseApi.create(FirebaseApi::class.java).getBarberias().execute()
        val barberias = call.body()
        return barberias
    }

    fun getBarberia(id: String): BarberiaResponse? {
        val call = firebaseApi.create(FirebaseApi::class.java).getBarberia(id).execute()
        val barberia = call.body()
        barberia?.id = id
        return barberia
    }

    fun setBarberia(barberia: BarberiaResponse): PostResponse? {
        val call = firebaseApi.create(FirebaseApi::class.java).setBarberia(barberia).execute()
        val results = call.body()
        return results
    }
}