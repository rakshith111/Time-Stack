package com.example.networkprototypeapp.httpclient

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

    fun makePostApiRequest(): TimeStackInterface {
        val okHttpClient = OkHttpClient.Builder()
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.108:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        apiInterface = retrofit.create(TimeStackInterface::class.java)
        return apiInterface
    }
}