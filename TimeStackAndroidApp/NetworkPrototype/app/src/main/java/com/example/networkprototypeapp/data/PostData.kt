package com.example.networkprototypeapp.data

import com.google.gson.annotations.SerializedName

data class PostData(
    @SerializedName("message") val message: String,
)