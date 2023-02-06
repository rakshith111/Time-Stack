package com.example.timestackprototype

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.*

const val msgThread = "thread"

/**
 * Start runnable
 *
 * @constructor Create empty Start runnable
 */
object StartRunnable {
    private lateinit var handler: Handler
    private lateinit var runnableToast: Runnable
    private lateinit var addressInput:String
    private lateinit var executor: ScheduledExecutorService
    private lateinit var task: ScheduledFuture<*>
    private var count:Int = 0
    var allMessages: MutableList<String> = mutableListOf("")

    /**
     * Start check function to initiate the network request
     *
     * @param txtView - TextView to display the data
     * @param EditTxtAddress - EditText containing the API address
     * @param context - Context of the app
     * @param btnGet - Button to initiate the request
     */
    fun startCheck(txtView: TextView, EditTxtAddress: EditText,context: Context, btnGet: Button){
        executor = Executors.newSingleThreadScheduledExecutor()
        task = executor.scheduleAtFixedRate({

           Log.i(msgThread, "Running on thread: ${Thread.currentThread().name}")
            makeNetworkRequest(txtView, EditTxtAddress, context, btnGet)
        }, 0, 30, TimeUnit.SECONDS)
    }

    /**
     * Make network request
     *
     * @param txtView - TextView to display the data
     * @param EditTxtAddress - EditText containing the API address
     * @param context - Context of the app
     * @param btnGet - Button to initiate the request
     */
         private fun makeNetworkRequest(txtView: TextView, EditTxtAddress: EditText, context: Context, btnGet: Button) {
        // Store the input address
            addressInput = EditTxtAddress.text.toString()
        // Check if the input is empty
            if (addressInput.isEmpty()) {
                // Display a toast message if the input is empty
                Toast.makeText(context, "Empty Input", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // Make the network request using Retrofit
                GetService().getTimeX(EditTxtAddress).message()
                    .enqueue(object : Callback<Map<String, Any>> {
                        @SuppressLint("SetTextI18n")
                        override fun onResponse(
                            call: Call<Map<String, Any>>,
                            response: Response<Map<String, Any>>
                        ) {
                            count = 0
                            if (response.isSuccessful) {
                                allMessages.clear()
                                val dumpData = response.body()
//                                val map = mapOf("dump" to mapOf("data0" to "HI @ 17:41:04"))
                                if (dumpData != null) {
                                    for ((key, value) in dumpData) {
                                        when (value) {
                                            is Map<*, *> -> for ((innerKey, innerValue) in value) {
                                                println("$key.$innerKey = $innerValue")
                                                allMessages.add("$innerValue")
//                                                txtView.text = "$innerValue"
                                            }
                                            else -> println("$key = $value")
                                        }
                                    }
                                }
                                Log.i(TAG1,"MESSAGE INFO")
                                Log.i(TAG1, "RAW, ${response.raw()}")
                                Log.i(TAG1, "BODY, ${response.body()}")

                                btnGet.isEnabled = false
                                btnGet.setBackgroundColor(Color.GREEN)
                                btnGet.text = "Connected"
                            } else {
                                when (response.code()) {
                                    404 -> Toast.makeText(
                                        context,
                                        "not found",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    500 -> Toast.makeText(
                                        context,
                                        "server broken",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    else -> Toast.makeText(
                                        context,
                                        "unknown error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            txtView.text = allMessages.toString()
                        }

                        /**
                         * On failure callback method that takes in a call and a Throwable (t) as
                         * parameters.
                         *
                         * @param call The call that failed.
                         * @param t The Throwable that caused the failure.
                         */

                        @SuppressLint("SetTextI18n")
                        override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                            Log.i(TAG2, "POST Failed\n DEBUG INFO")
                            Log.d(TAG2, "$t")

                            if(count == 0){
                                // create a handler for the main looper
                                handler = Handler(Looper.getMainLooper())
                                runnableToast = Runnable {
                                    // display a message
                                   Toast.makeText(context, "runnable stopped", Toast.LENGTH_LONG)
                                       .show()
                               }
                                // post the runnable after 5000ms
                                handler.postDelayed(runnableToast, 5000)

                            }
                            // shut down the executor or ScheduledExecutor
                            executor.shutdown()
                            task.cancel(false)
                            btnGet.setBackgroundColor(Color.RED)
                            btnGet.isEnabled = true
                            btnGet.text = "connect"
                            if(count != 5){
                                Log.i(TAG1,"Retrying $count")
                                count++
                                // start the check again
                                startCheck(txtView, EditTxtAddress, context, btnGet)
                                Toast.makeText(context, "Retrying $count", Toast.LENGTH_LONG)
                                    .show()
                            }

                        }
                    })
            }
        }
}