package com.example.timestackprototype

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TimeInterface {
    @GET ("/dump")
//    fun message(): Response<Dump>
    fun message(): Call<Map<String, Any>>

    @POST ("/receive")
    fun messageUser(@Body messageData: TimeApi): Call<TimeApi>
}
