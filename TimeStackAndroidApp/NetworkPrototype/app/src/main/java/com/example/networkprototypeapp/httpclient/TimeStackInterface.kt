package com.example.networkprototypeapp.httpclient

import com.example.networkprototypeapp.data.FakeData
import com.example.networkprototypeapp.data.PostData
import com.example.networkprototypeapp.data.ReceiveData
import com.example.networkprototypeapp.data.TimeStackData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TimeStackInterface {
    @GET("/data")
    fun getMessage(): Call<ReceiveData>

    @POST("/webhook")
    fun postMessage(@Body messageData: TimeStackData):Call<PostData>

}

interface TimeStackInterfaceFake {
    @GET("/posts")
    fun getMessage(): Call<FakeData>

}