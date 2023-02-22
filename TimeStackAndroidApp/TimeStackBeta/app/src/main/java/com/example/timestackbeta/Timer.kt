package com.example.timestackbeta

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class StackTimer {
    private lateinit var executor: ScheduledExecutorService
    private lateinit var task: ScheduledFuture<*>
    private var time: Int = 0

    fun startTimer(totalPlayedTime: Int) {
        time = totalPlayedTime

        executor = Executors.newSingleThreadScheduledExecutor()
        task = executor.scheduleAtFixedRate({
            println(time)
                    time++
        }, 0, 1, TimeUnit.SECONDS)
    }
    fun stopTimer(pauseTimer:(Int) -> Unit){
        pauseTimer(time)
        time = 0
        executor.shutdown()
        task.cancel(false)
    }
}