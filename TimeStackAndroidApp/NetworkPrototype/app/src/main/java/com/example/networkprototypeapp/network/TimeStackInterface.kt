package com.example.networkprototypeapp.network

import com.example.networkprototypeapp.data.FakeData
import com.example.networkprototypeapp.data.FakeDataItem
import com.example.networkprototypeapp.data.PostData
import com.example.networkprototypeapp.data.TimeStackData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TimeStackInterface {
    @GET("send_req")
    fun getMessage(): Call<Map<String, Any>>

    @POST("/webhook")
    fun postMessage(@Body messageData: TimeStackData):Call<PostData>

}

interface TimeStackInterfaceFake {
    @GET("/posts")
    fun getMessage(): Call<FakeData>

}