package com.example.timestackarchitecture.data

import android.content.Context

class SharedPreferencesRepository(context: Context){
    private val sharedPref = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    fun saveProgress(progress: Int) {
        sharedPref.edit().putInt("progress", progress).apply()
    }

    fun getProgress(): Int {
        return sharedPref.getInt("progress", 0)
    }
}