package com.example.timestackprototype

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
class MainActivity : AppCompatActivity() {
    private lateinit var addressInput:String
    private lateinit var messageInput:String
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
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
                         @SuppressLint("SetTextI18n")
                         override fun onResponse(call: Call<Map<String, Any>>,
                                                 response: Response<Map<String, Any>>) {
                            if (response.isSuccessful) {
                                Log.i(TAG1, "RAW, ${response.raw()}")
                                Log.i(TAG1, "BODY, ${response.body()}")
                                Toast.makeText(this@MainActivity, "Server is Up, Starting runnable",
                                    Toast.LENGTH_SHORT).show()
                                StartRunnable.startCheck(binding.txtView, binding.EditTxtAddress
                                    , applicationContext, binding.btnGet)

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
                        }

                        override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                            Log.i(TAG2, "POST Failed\n DEBUG INFO")
                            Log.d(TAG2, "$t")
                            handler = Handler(Looper.getMainLooper())
                            runnable = Runnable {
                                Toast.makeText(this@MainActivity,"$t", Toast.LENGTH_LONG)
                                    .show()
                            }
                            handler.postDelayed(runnable, 5000)
                            Toast.makeText(this@MainActivity,"Server is Down", Toast.LENGTH_LONG)
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
                        Log.i(TAG1, "Message sent")
                            binding.EditTxtMessage.setText("")
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
                    Toast.makeText(this@MainActivity, "Data Successfully POSTED ", Toast.LENGTH_SHORT)
                        .show()

                    onResult(addedMessage)
                }
            }
        )
    }
}







