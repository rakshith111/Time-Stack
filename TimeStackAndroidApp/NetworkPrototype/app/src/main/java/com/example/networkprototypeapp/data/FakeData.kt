package com.example.networkprototypeapp.data

class FakeData : ArrayList<FakeDataItem>()

data class FakeDataItem(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int
)