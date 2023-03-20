package com.example.timestackarchitecture.ui.components

import com.example.timestackarchitecture.viewmodels.TimerViewModel
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class StackTimer {
    companion object StartOrStop {
        private lateinit var executor: ScheduledExecutorService
        private lateinit var task: ScheduledFuture<*>
        private var time: Int = 0
        private lateinit var timerViewModel: TimerViewModel
        fun startTimer(totalPlayedTime: Int, duration: Int) {
            time = totalPlayedTime

            executor = Executors.newSingleThreadScheduledExecutor()
            task = executor.scheduleAtFixedRate({
                println("time $time")
                time++
                if (time >= duration/ 1000) {
                    stopTimer { time ->
                        timerViewModel.saveProgress(time)
                    }
                }
            }, 0, 1, TimeUnit.SECONDS)
        }
        fun stopTimer(pauseTimer:(Int) -> Unit){
            pauseTimer(time)
            time = 0
            executor.shutdown()
            task.cancel(false)
            println("stopped")
        }
    }

}