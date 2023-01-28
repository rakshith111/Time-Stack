package com.example.timestackprototype

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.timestackprototype.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException
const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnGet.setOnClickListener {
                try {
                    TimeInterface.api.message()
                } catch(e: IOException) {
                    Log.e(TAG, "IOException, you might not have internet connection")
                    txtView.text = getString(R.string.Error)
                } catch (e: HttpException) {
                    Log.e(TAG, "HttpException, unexpected response")
                    txtView.text = getString(R.string.Error)
                }
            }
        }
    }

}