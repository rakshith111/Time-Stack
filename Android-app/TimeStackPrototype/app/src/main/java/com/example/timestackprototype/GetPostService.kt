package com.example.timestackprototype

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetService {
    private lateinit var timeX: TimeInterface
    fun getTimeX():TimeInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.106:8000")
            .addConverterFactory(GsonConverterFactory.create()) //Use Gson
            .build()

        timeX = retrofit.create(TimeInterface::class.java)
        return timeX
    }

}

class PostService {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.106:8000") // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}
