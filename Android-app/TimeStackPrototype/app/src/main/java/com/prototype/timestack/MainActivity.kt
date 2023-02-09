package com.prototype.timestack

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.timestackprototype.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import retrofit2.*
const val TAG1 = "Retro_clean"
const val TAG2 = "Retro_dirty"

/**
 * Main activity class extending AppCompatActivity
 *
 */
class MainActivity : AppCompatActivity() {
    // Variables to store the address and message input
    private lateinit var addressInput: String
    private lateinit var messageInput: String

    // Variables for handling error messages
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    // Data binding instance
    private lateinit var binding: ActivityMainBinding

    /**
     * Overridden onCreate method to set up the activity
     *
     * @param savedInstanceState Bundle to save the state of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using data binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set an onClickListener for the "Get" button
        binding.apply {
            btnGet.setOnClickListener {
                // Get the text from the address input field
                addressInput = binding.EditTxtAddress.text.toString()
                if (addressInput.isEmpty()) {
                    // Show a toast message if the input is empty
                    Toast.makeText(this@MainActivity, "Empty Input", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // Make a GET request using the GetService
                    GetService().getTimeX(binding.EditTxtAddress).message()
                        .enqueue(object : Callback<Map<String, Any>> {
                            /**
                             * Callback method for handling the response
                             *
                             * @param call Call object representing the API call
                             * @param response Response object containing the API response
                             */
                            @SuppressLint("SetTextI18n")
                            override fun onResponse(
                                call: Call<Map<String, Any>>,
                                response: Response<Map<String, Any>>
                            ) {
                                if (response.isSuccessful) {
                                    // Log the raw and body responses
                                    Log.i(TAG1, "RAW, ${response.raw()}")
                                    Log.i(TAG1, "BODY, ${response.body()}")
                                    // Show a toast message indicating the server is up
                                    Toast.makeText(
                                        this@MainActivity, "Server is Up, Starting runnable",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // Start the runnable using the StartRunnable class
                                    StartRunnable.startCheck(
                                        binding.txtView,
                                        binding.EditTxtAddress,
                                        applicationContext,
                                        binding.btnGet
                                    )

                                } else {
                                    // Handle different error codes
                                    when (response.code()) {
                                        404 -> Toast.makeText(
                                            this@MainActivity,
                                            "not found",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        500 -> Toast.makeText(
                                            this@MainActivity,
                                            "server broken",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        else -> Toast.makeText(
                                            this@MainActivity,
                                            "unknown error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                            /**
                             * The onFailure method is called when the retrofit call to a
                             * server fails

                             *
                             * @param call The call that failed.
                             * @param t The Throwable that caused the failure.
                             */
                            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                                // Logging the error message for debugging purposes
                                Log.i(TAG2, "POST Failed\n DEBUG INFO")
                                Log.d(TAG2, "$t")

                                // Initializing a handler to show the error message on the main thread
                                handler = Handler(Looper.getMainLooper())
                                runnable = Runnable {
                                    Toast.makeText(this@MainActivity, "$t", Toast.LENGTH_LONG)
                                        .show()
                                }

                                // Showing the error message on the main thread after 5 seconds
                                handler.postDelayed(runnable, 5000)

                                // Showing a fallback error message on the main thread immediately
                                Toast.makeText(
                                    this@MainActivity,
                                    "Server is Down",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        })
                }


            }

        }

        binding.apply {
            btnPost.setOnClickListener {
                messageInput = binding.EditTxtMessage.text.toString()
                if (messageInput.isNotEmpty() && addressInput.isNotEmpty()) {
                    // Logging the message input for debugging purposes
                    println(messageInput)

                    // Launching a coroutine to send the request
                    lifecycleScope.launch {
                        sendReq(TimeApi(message = messageInput)) {
                            // Logging a successful message send for debugging purposes
                            Log.i(TAG1, "Message sent")

                            // Clearing the EditText field after the request is sent
                            binding.EditTxtMessage.setText("")
                        }
                    }
                } else {
                    // Showing an error Toast if the EditText field is empty
                    Toast.makeText(this@MainActivity, "Empty Input", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.apply {
            btnPost.setOnClickListener {
                messageInput = binding.EditTxtMessage.text.toString()
                if (messageInput.isNotEmpty() && addressInput.isNotEmpty()) {
                    // Logging the message input for debugging purposes
                    println(messageInput)

                    // Launching a coroutine to send the request
                    lifecycleScope.launch {
                        sendReq(TimeApi(message = messageInput)) {
                            // Logging a successful message send for debugging purposes
                            Log.i(TAG1, "Message sent")
                            //thread name
                            Log.i(msgThread, "Running on thread for POST request: ${Thread.currentThread().name}")
                            // Clearing the EditText field after the request is sent
                            binding.EditTxtMessage.setText("")
                        }
                    }
                } else {
                    // Showing an error Toast if the EditText field is empty
                    Toast.makeText(this@MainActivity, "Empty Input", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    // Defining a private function to send a POST request to a server
    private fun sendReq(messageData: TimeApi, onResult: (TimeApi?) -> Unit) {
        // Building a retrofit service using the address input from an EditText field (EditTxtAddress)
        val retrofit =
            PostService(binding.EditTxtAddress).buildService(TimeInterface::class.java)

        // Enqueueing the retrofit call to send the POST request
        retrofit.messageUser(messageData).enqueue(
            object : Callback<TimeApi> {
                // On failure, show an error Toast and call the onResult function with a null argument
                override fun onFailure(call: Call<TimeApi>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT)
                        .show()
                    onResult(null)
                }

                /**
                 * On response
                 *
                 * @param call - The call object to retrieve the response
                 * @param response - The response from the server
                 */
                // On response, show a success Toast and call the onResult function with the response body
                override fun onResponse(call: Call<TimeApi>, response: Response<TimeApi>) {
                    val addedMessage = response.body()
                    Toast.makeText(
                        this@MainActivity,
                        "Data Successfully POSTED ",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    onResult(addedMessage)
                }
            }
        )
    }
}










