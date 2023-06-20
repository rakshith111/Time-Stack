package com.example.networkprototypeapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.networkprototypeapp.data.FakeData
import com.example.networkprototypeapp.network.GetService
import com.example.networkprototypeapp.network.GetServiceFake
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiViewModel : ViewModel() {
    private var _dashboardData = MutableStateFlow<Map<String, Any>>(mapOf())

    val dashboardData: MutableStateFlow<Map<String, Any>> = _dashboardData

    private var _dashboardFakeData = MutableStateFlow(FakeData())

    val dashboardFakeData: MutableStateFlow<FakeData> = _dashboardFakeData

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

    fun makeGetApiRequestFake(){
        GetServiceFake().getApiInterface( "https://jsonplaceholder.typicode.com").getMessage()
            .enqueue(object : Callback<FakeData>{
                override fun onResponse(
                    call: Call<FakeData>,
                    response: Response<FakeData>
                ) {
                    println("Response: ${response.body()}")
                    println("Response: ${response.raw()}")
                    // Process the response data here
                    _dashboardFakeData.value = (response.body() ?: FakeData())
                }

                override fun onFailure(call: Call<FakeData>, t: Throwable) {
                    println("Error: ${t.message}")
                    // Handle the error here
                }
            })
    }
}