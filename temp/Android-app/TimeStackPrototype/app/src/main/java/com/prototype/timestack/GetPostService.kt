package com.prototype.timestack

import android.widget.EditText
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * GetService class is responsible for creating the TimeInterface object by using Retrofit Library.
 *
 * This class includes the implementation of getTimeX function that returns TimeInterface object
 * which can be used to make API calls to the provided address.
 */
class GetService {
    // TimeInterface object to store the time information
    private lateinit var timeX: TimeInterface
    // Address input from the user
    private lateinit var addressInput:String

    /**
     * Get time x
     *
     * @param editTxtAddress: EditText object that contains the address input by the user
     * @return TimeInterface object created using the address input provided by the user
     */
    fun getTimeX(editTxtAddress: EditText): TimeInterface {
        // Store the address input in the addressInput variable
        addressInput = editTxtAddress.text.toString()
        // Create a new Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(addressInput) // Set the base url to the address input provided by the user
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson for converting response
            .build()

        // Create the TimeInterface object using the retrofit instance
        timeX = retrofit.create(TimeInterface::class.java)
        return timeX
    }

}

/**
 * Class to handle post service
 *
 * @param editTxtAddress The address input, in the form of an EditText object
 */
class PostService(editTxtAddress: EditText) {
    // OkHttp client to handle the network connections
    private val client = OkHttpClient.Builder().build()

    // String variable to store the address input
    private var addressInput:String = editTxtAddress.text.toString()

    // Retrofit object to handle the REST API requests
    private val retrofit = Retrofit.Builder()
        .baseUrl(addressInput) // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create()) // use Gson as the JSON converter
        .client(client) // set the OkHttp client
        .build()

    /**
     * Function to build the service for the specified interface
     *
     * @param T The interface type to be built
     * @param service The Class object of the interface
     * @return An instance of the built service
     */

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}
