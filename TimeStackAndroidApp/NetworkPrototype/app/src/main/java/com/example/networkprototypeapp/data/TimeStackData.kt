package com.example.networkprototypeapp.data

import com.google.gson.annotations.SerializedName

data class TimeStackData(
    @SerializedName("message") val message: String,
    @SerializedName("data")val data: String
)