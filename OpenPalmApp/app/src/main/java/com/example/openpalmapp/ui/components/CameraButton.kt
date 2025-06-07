package com.example.openpalmapp.ui.components

import android.content.Intent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.openpalmapp.CameraActivity

@Composable
fun OpenCameraButton(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Button(
        onClick = {
            context.startActivity(Intent(context, CameraActivity::class.java))
        },
        modifier = modifier
    ) {
        Text("Open Camera")
    }
}