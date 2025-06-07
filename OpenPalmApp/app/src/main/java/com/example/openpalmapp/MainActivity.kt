package com.example.openpalmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.openpalmapp.ui.components.EmailInput
import com.example.openpalmapp.ui.theme.OpenPalmAppTheme
// for submit button
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

// for camera Button
import com.example.openpalmapp.ui.components.OpenCameraButton
// for api service
import com.example.openpalmapp.network.sendEmailToApiGateway

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenPalmAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Joel",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize()) {
        // 👆 Snackbar at the top
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hello $name!",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            EmailInput(
                email = email,
                onEmailChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                isLoading = isLoading,
                onSubmit = {
                    coroutineScope.launch {
                        isLoading = true
                        val result = sendEmailToApiGateway(email)
                        isLoading = false

                        val message = when {
                            !result.success -> "❌ ${result.message}"
                            result.emailSent -> "✅ ${result.message}"
                            else -> "ℹ️ ${result.message}"
                        }
                        snackbarHostState.showSnackbar(message)
                    }
                }
            )

            Spacer(modifier = Modifier.height(56.dp))

            OpenCameraButton(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OpenPalmAppTheme {
        Greeting("Joel")
    }
}