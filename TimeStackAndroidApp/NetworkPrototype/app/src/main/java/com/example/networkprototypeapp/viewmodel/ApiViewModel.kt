package com.example.networkprototypeapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.networkprototypeapp.data.TimeStackData
import com.example.networkprototypeapp.network.GetService
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiViewModel : ViewModel() {
    private var _dashboardData = MutableStateFlow<Map<String, Any>>(mapOf())

    val dashboardData: MutableStateFlow<Map<String, Any>> = _dashboardData

    fun makeGetApiRequest() {
        GetService().getApiInterface("").getMessage()
            .enqueue(object : Callback<Map<String, Any>> {
                override fun onResponse(
                    call: Call<Map<String, Any>>,
                    response: Response<Map<String, Any>>
                ) {
                    println("Response: ${response.body()}")
                    println("Response: ${response.raw()}")
                    // Process the response data here
                    _dashboardData.value = response.body() ?: mapOf()
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    println("Error: ${t.message}")
                    // Handle the error here
                }
            })
    }
}