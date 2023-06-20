package com.example.networkprototypeapp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetServiceFake {
    private lateinit var apiInterface: TimeStackInterfaceFake

    fun getApiInterface(baseUrl: String): TimeStackInterfaceFake {
        val okHttpClient = OkHttpClient.Builder()
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiInterface = retrofit.create(TimeStackInterfaceFake::class.java)
        return apiInterface
    }
}