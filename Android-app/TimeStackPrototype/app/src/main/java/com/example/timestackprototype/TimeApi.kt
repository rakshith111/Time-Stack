package com.example.timestackprototype

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface TimeApi {
    @GET ("/receive")
    fun message(): Response<List<TimeDump>>
}