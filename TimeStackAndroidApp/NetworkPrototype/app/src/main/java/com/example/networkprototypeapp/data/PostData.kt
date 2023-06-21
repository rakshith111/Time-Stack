package com.example.networkprototypeapp.data

import com.google.gson.annotations.SerializedName

data class PrototypeData(
    @SerializedName("message") val message: String,
    @SerializedName("data")val data: String

)