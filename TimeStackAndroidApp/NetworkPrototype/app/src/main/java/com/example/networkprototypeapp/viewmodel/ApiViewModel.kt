package com.example.networkprototypeapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.networkprototypeapp.data.FakeData
import com.example.networkprototypeapp.data.PostData
import com.example.networkprototypeapp.data.TimeStackData
import com.example.networkprototypeapp.network.GetService
import com.example.networkprototypeapp.network.GetServiceFake
import com.example.networkprototypeapp.network.PostService
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
        GetService().getApiInterface("http://localhost:8000").getMessage()
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

    fun makePostApiRequest(){
        PostService().makePostApiRequest().postMessage(TimeStackData("Hello World", "2021-09-01"))
            .enqueue(object : Callback<PostData> {

                override fun onResponse(call: Call<PostData>, response: Response<PostData>) {
                    println("Response: ${response.body()}")
                    println("Response: ${response.raw()}")
                    // Process the response data here
                    println("Success")
                }

                override fun onFailure(call: Call<PostData>, t: Throwable) {
                    println("Error: ${t.message}")
                }
            }
            )
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