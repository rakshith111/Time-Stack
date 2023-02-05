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
import androidx.core.os.postDelayed
import com.example.timestackprototype.StartRunnable.runnable
import com.google.gson.annotations.Until
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object StartRunnable {
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var runnableToast: Runnable
    private lateinit var addressInput:String
    private var count:Int = 0
    var allMessages: MutableList<String> = mutableListOf("")
    fun startCheck(txtView: TextView, EditTxtAddress: EditText,context: Context, btnGet: Button){
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            makeNetworkRequest(txtView, EditTxtAddress, context, btnGet)
            handler.postDelayed(runnable, 30000) // Repeat task every 30 seconds
            Log.e(TAG1,"GETTING data after 30 secs")
        }
        handler.postDelayed(runnable, 30)
    }

         private fun makeNetworkRequest(txtView: TextView, EditTxtAddress: EditText, context: Context, btnGet: Button) {
            addressInput = EditTxtAddress.text.toString()
            if (addressInput.isEmpty()) {
                Toast.makeText(context, "Empty Input", Toast.LENGTH_SHORT)
                    .show()
            } else {
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

                                Log.e(TAG1, "RAW, ${response.raw()}")
                                Log.e(TAG1, "BODY, ${response.body()}")
                                Log.e(TAG1, "HEADERS, ${response.headers()}")
                                Log.e(TAG1, "CODE, ${response.code()}")

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

                        @SuppressLint("SetTextI18n")
                        override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                            Log.e(TAG2, "NO")
                            Log.e(TAG2, "$t")
                            if(count == 0){
                                println("the fuckkkk")
//                                runnable = Runnable {
//                                    Toast.makeText(context, "$t", Toast.LENGTH_LONG)
//                                        .show()
//                                }
//                                handler.postDelayed(runnable, 5000)
                                handler = Handler(Looper.getMainLooper())
                                runnableToast = Runnable {
                                   Toast.makeText(context, "runnable stopped", Toast.LENGTH_LONG)
                                       .show()
                               }
                                handler.postDelayed(runnableToast, 5000)

                            }

                            handler.removeCallbacks(runnable)
                            btnGet.setBackgroundColor(Color.RED)
                            btnGet.isEnabled = true
                            btnGet.text = "connect"
                            if(count != 4){
                                count++
                                startCheck(txtView, EditTxtAddress, context, btnGet)
                                Toast.makeText(context, "Retrying $count", Toast.LENGTH_LONG)
                                    .show()
                            }

                        }
                    })
            }
        }
}