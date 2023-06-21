package com.example.networkprototypeapp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetService {
    private lateinit var apiInterface: TimeStackInterface

    fun getApiInterface(baseUrl: String): TimeStackInterface {
        val okHttpClient = OkHttpClient.Builder()
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiInterface = retrofit.create(TimeStackInterface::class.java)
        return apiInterface
    }
}

class PostService{
    private lateinit var apiInterface: TimeStackInterface
    private val client = OkHttpClient.Builder().build()
    fun makePostApiRequest(): TimeStackInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.101:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        apiInterface = retrofit.create(TimeStackInterface::class.java)
        return apiInterface
    }
}