package com.example.timestackprototype

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.timestackprototype.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import retrofit2.*


const val TAG1 = "Retro_clean"
const val TAG2 = "Retro_dirty"
class MainActivity : AppCompatActivity() {
    private lateinit var addressInput:String
    private lateinit var messageInput:String
    var allMessages: MutableList<String> = mutableListOf("")
    private lateinit var binding: ActivityMainBinding
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //runnable for every 30sec
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            makeNetworkRequest(binding.txtView)
            handler.postDelayed(runnable, 30000) // Repeat task every 30 seconds
        }
        handler.postDelayed(runnable, 300)
        binding.apply {
            btnGet.setOnClickListener {
                addressInput = binding.EditTxtAddress.text.toString()
                if(addressInput.isEmpty()){
                    Toast.makeText(this@MainActivity,"Empty Input", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    GetService().getTimeX(binding.EditTxtAddress).message().enqueue(object : Callback<Map<String, Any>> {
                         override fun onResponse(call: Call<Map<String, Any>>,
                                                 response: Response<Map<String, Any>>) {
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
                            } else {
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
                             txtView.text = allMessages.toString()
                        }

                        override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                            Log.e(TAG2, "NO")
                            Log.e(TAG2, "$t")
                            Toast.makeText(this@MainActivity,"$t", Toast.LENGTH_LONG)
                                .show()
                        }
                    })
                }


            }

        }

        binding.apply {
            btnPost.setOnClickListener{
                messageInput = binding.EditTxtMessage.text.toString()
                if(messageInput.isNotEmpty() && addressInput.isNotEmpty()) {
                    println(messageInput)
                    lifecycleScope.launch{
                        sendReq(TimeApi(message = messageInput)){
                        Log.e(TAG1, "Message sent")
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity,"Empty Input", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
    private fun sendReq(messageData: TimeApi, onResult: (TimeApi?) -> Unit){
        val retrofit = PostService(binding.EditTxtAddress).buildService(TimeInterface::class.java)
        retrofit.messageUser(messageData).enqueue(
            object : Callback<TimeApi> {
                override fun onFailure(call: Call<TimeApi>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT)
                        .show()
                    onResult(null)
                }
                override fun onResponse( call: Call<TimeApi>, response: Response<TimeApi>) {
                    val addedMessage = response.body()
                    Toast.makeText(this@MainActivity, "Data added to API", Toast.LENGTH_SHORT)
                        .show()
                    onResult(addedMessage)
                }
            }
        )
    }

    private fun makeNetworkRequest(txtView: TextView) {
            addressInput = binding.EditTxtAddress.text.toString()
                if(addressInput.isEmpty()){
                    Toast.makeText(this@MainActivity,"Empty Input", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    GetService().getTimeX(binding.EditTxtAddress).message()
                        .enqueue(object : Callback<Map<String, Any>> {
                            override fun onResponse(
                                call: Call<Map<String, Any>>,
                                response: Response<Map<String, Any>>
                            ) {
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
                                } else {
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
                                txtView.text = allMessages.toString()
                            }

                            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                                Log.e(TAG2, "NO")
                                Log.e(TAG2, "$t")
                                Toast.makeText(this@MainActivity, "$t", Toast.LENGTH_LONG)
                                    .show()
                            }
                        })
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

}





