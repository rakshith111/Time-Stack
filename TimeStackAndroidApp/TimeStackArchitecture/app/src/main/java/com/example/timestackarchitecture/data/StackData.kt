package com.example.timestackarchitecture.data

data class StackData(
    val id: Int,
    val stackName: String,
    val stackTime: Long,
    var activeStack: Boolean,
    var totalPlayedTime: Int,
    var isPlaying: Boolean,
)
