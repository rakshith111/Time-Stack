package com.example.timestackprototype

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.timestackprototype.databinding.ActivityMainBinding
import retrofit2.*


const val TAG1 = "Retro_clean"
const val TAG2 = "Retro_dirty"
class MainActivity : AppCompatActivity() {
    private lateinit var addressInput:String
    private lateinit var messageInput:String
    var allMessages: MutableList<String> = mutableListOf("")
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btnGet.setOnClickListener {
                addressInput = binding.EditTxtAddress.text.toString()
                if(addressInput.isEmpty()){
                    Toast.makeText(this@MainActivity,"Empty Input", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    GetService().getTimeX(binding.EditTxtAddress).message().enqueue(object : Callback<Map<String, Any>> {
                         override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
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
                                Log.e(TAG2, "RAW, ${response.raw()}")
                                Log.e(TAG2, "BODY, ${response.body()}")
                                Log.e(TAG2, "HEADERS, ${response.headers()}")
                                Log.e(TAG2, "CODE, ${response.code()}")
                                //Toast error
                            }
                        }

                        override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                            Log.e(TAG2, "NO")
                            //toast error
                        }
                    })
                }
                    txtView.text = allMessages.toString()

            }
        }

        binding.apply {
            btnPost.setOnClickListener{
                messageInput = binding.EditTxtMessage.text.toString()
                if(messageInput.isNotEmpty() && addressInput.isNotEmpty()) {
                    println(messageInput)
                    sendReq(TimeApi(message = messageInput)){
                        Log.e(TAG1, "Message sent")
                        Toast.makeText(this@MainActivity,"Post Message = $messageInput", Toast.LENGTH_SHORT)
                            .show()
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
                    onResult(null)
                }
                override fun onResponse( call: Call<TimeApi>, response: Response<TimeApi>) {
                    val addedUser = response.body()
                    onResult(addedUser)
                }
            }
        )
    }
}

