package com.example.timestackbeta20

import androidx.lifecycle.ViewModel
import com.example.timestackbeta20.data.ActivityData

class ActivityViewModel : ViewModel(){
    val stacks = mutableListOf<ActivityData>()

}
