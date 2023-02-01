//package com.example.timestackprototype
//
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//
//
//
//class Retro {
//    lateinit var retro:Retrofit
//    fun retros():Retrofit{
//        retro = Retrofit.Builder()
//            .baseUrl("https://jsonplaceholder.typicode.com/")
//            .addConverterFactory(GsonConverterFactory.create()) //Use Gson
//            .build()
//        return retro
//    }
//
//}