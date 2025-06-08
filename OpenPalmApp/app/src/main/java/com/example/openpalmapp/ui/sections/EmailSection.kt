package com.example.openpalmapp.ui.sections

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.openpalmapp.network.sendEmailToApiGateway
import com.example.openpalmapp.ui.components.EmailInput
import kotlinx.coroutines.launch

@Composable
fun EmailSection(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    EmailInput(
        email = email,
        onEmailChange = { email = it },
        isLoading = isLoading,
        modifier = modifier,
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
}
