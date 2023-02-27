package com.example.timestackbeta20.data

import androidx.compose.runtime.*

data class ActivityData(
    val id : Int,
    val stackName : String,
    val stackTime : String,
    val activeStack : String,
    val stackProgress : Int
)