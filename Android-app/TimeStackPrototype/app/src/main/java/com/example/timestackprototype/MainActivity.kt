package com.example.timestackprototype

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.format.Time
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.timestackprototype.databinding.ActivityMainBinding
import retrofit2.*


const val TAG1 = "Retro_clean"
const val TAG2 = "Retro_dirty"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btnGet.setOnClickListener {
                GetService().getTimeX().message().enqueue(object : Callback<Map<String, Any>> {
                    override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                        if (response.isSuccessful) {
                            val dumpData = response.body()
                            val map = mapOf("dump" to mapOf("data0" to "HI @ 17:41:04"))
                            if (dumpData != null) {
                                for ((key, value) in dumpData) {
                                    when (value) {
                                        is Map<*, *> -> for ((innerKey, innerValue) in value) {
                                            println("$key.$innerKey = $innerValue")
                                            txtView.text = "$innerValue"
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


        }

        binding.apply {
            btnPost.setOnClickListener{
                fun sendReq(messageData: TimeApi , onResult: (TimeApi?) -> Unit){
                    val retrofit = PostService().buildService(TimeInterface::class.java)
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

                sendReq(TimeApi(message = "HELLO")){
                    Log.e(TAG1, "Message sent")
                }
            }
        }
    }
}

