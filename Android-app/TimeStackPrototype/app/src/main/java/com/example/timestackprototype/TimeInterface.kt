package com.example.timestackprototype

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object TimeInterface {
    val api:TimeApi by lazy{
        Retrofit.Builder()
            .baseUrl("http://127.0.0.1:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TimeApi::class.java)
    }
}