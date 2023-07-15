package com.example.websocket_app.presentation

import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.example.websocket_app.components.QrCodeAnalyzer
import com.example.websocket_app.components.ScanningAnimation
import com.example.websocket_app.viewmodel.QrViewModel
import com.google.common.util.concurrent.ListenableFuture

@Composable
fun QrScanner(
    hasCamPermission: Boolean,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    lifecycleOwner: LifecycleOwner,
    qrViewModel: QrViewModel,
    sendCode: () -> Unit,
    scanCount: Int = 0
) {
    val scanCounter = remember { mutableStateOf(scanCount) }
    var code by remember { mutableStateOf("") }
    DisposableEffect(lifecycleOwner) {
        onDispose { cameraProviderFuture.get().unbindAll() }
    }
    val context = LocalContext.current
    LaunchedEffect(code) {
        if (code.isNotEmpty()) {
            Toast.makeText(context, "Scanned QR Code: $code", Toast.LENGTH_LONG).show()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (hasCamPermission) {
            Box(
                modifier = Modifier
                    .aspectRatio(1F)  // Adjust the size of the camera view as needed
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                AndroidView(
                    factory = { context ->
                        val previewView = PreviewView(context)
                        val preview = Preview.Builder().build()
                        val selector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()
                        preview.setSurfaceProvider(previewView.surfaceProvider)
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setImageQueueDepth(1)
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        imageAnalysis.setAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            QrCodeAnalyzer { result ->
                                code = result
                                qrViewModel.qrCode = result
                                if (scanCounter.value == 0) {
                                    scanCounter.value = 1
                                    sendCode()
                                }
                            }
                        )
                        try {
                            cameraProviderFuture.get().bindToLifecycle(
                                lifecycleOwner,
                                selector,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        previewView
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                )
                ScanningAnimation()
            }
        }
    }
    Text(
        text = code,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(32.dp),
        color = androidx.compose.ui.graphics.Color.White,
        style = MaterialTheme.typography.bodyLarge
    )
}
