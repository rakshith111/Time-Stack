package com.example.timestackprototype

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * TimeInterface: Defines the API endpoints for the Time app.
 * Uses the Retrofit library to handle network requests and API calls.
 */
interface TimeInterface {

    /**
     * message: A GET request to the "/dump" endpoint.
     * @return Call<Map<String, Any>>: Returns a Call object containing a Map of String and Any objects.
     */
    @GET("/dump")
    fun message(): Call<Map<String, Any>>

    /**
     * messageUser: A POST request to the "/receive" endpoint with a TimeApi object as the request body.
     * @param messageData: TimeApi object to be sent as the request body.
     * @return Call<TimeApi>: Returns a Call object containing a TimeApi object.
     */
    @POST("/receive")
    fun messageUser(@Body messageData: TimeApi):Call<TimeApi>
}