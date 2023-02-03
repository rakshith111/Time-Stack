package com.example.timestackprototype

import android.widget.EditText
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetService {
    private lateinit var timeX: TimeInterface
    private lateinit var addressInput:String
    fun getTimeX(editTxtAddress: EditText):TimeInterface {
        addressInput = editTxtAddress.text.toString()
        val retrofit = Retrofit.Builder()
            .baseUrl(addressInput)
            .addConverterFactory(GsonConverterFactory.create()) //Use Gson
            .build()

        timeX = retrofit.create(TimeInterface::class.java)
        return timeX
    }

}


class PostService(editTxtAddress: EditText) {
    private val client = OkHttpClient.Builder().build()
    private var addressInput:String = editTxtAddress.text.toString()
    private val retrofit = Retrofit.Builder()
        .baseUrl(addressInput) // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}
