package com.example.lottiesampleanimation

import android.graphics.Point
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toPointF
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.*
import com.example.lottiesampleanimation.ui.theme.LottieSampleAnimationTheme

class MainActivity : ComponentActivity() {

    private var timeRemaining: Long = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottieSampleAnimationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                    Loader()
                }
            }

        }

    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun Loader() {
    var timeRemaining by remember { mutableStateOf(0) }
    val totalTime = 100
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))

            val point = remember { Point() }
            val dynamicProperties = rememberLottieDynamicProperties(
                rememberLottieDynamicProperty(LottieProperty.TRANSFORM_POSITION, "Body") { frameInfo ->
                    var startY = frameInfo.startValue.y.toInt()
                    var endY = frameInfo.endValue.y.toInt()
                    val timeFactor = timeRemaining
                    when {
                        startY > endY -> startY -= timeFactor
                        else -> endY += timeFactor
                    }
                    point.set(frameInfo.startValue.x.toInt(), lerp(startY, endY, frameInfo.interpolatedKeyframeProgress.toInt()))
                    point.toPointF()
                }
            )

            LottieAnimation(
                composition,
                progress = {
                    println(timeRemaining.toFloat())
                    timeRemaining.toFloat()/totalTime

                },
                dynamicProperties = dynamicProperties
            )

            object : CountDownTimer(100000, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    timeRemaining = (millisUntilFinished / 1000).toInt()
//                    println(timeRemaining)
                }

                override fun onFinish() {
                    println("over")
                }

            }.start()
        }
    )
}

private fun lerp(valueA: Int, valueB: Int, progress: Int): Int {
    val smallerY = minOf(valueA, valueB)
    val largerY = maxOf(valueA, valueB)
    return smallerY + progress * (largerY - smallerY)
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LottieSampleAnimationTheme {
        Greeting("Android")
    }
}