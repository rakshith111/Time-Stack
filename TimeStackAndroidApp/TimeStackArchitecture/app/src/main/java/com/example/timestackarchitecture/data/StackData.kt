package com.example.timestackarchitecture.data

data class StackData(
    val id : Int,
    val stackName : String,
    val stackTime : Long,
    val activeStack : Boolean,
    val totalPlayedTime : Int
)
