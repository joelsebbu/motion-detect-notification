package com.example.openpalmapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun isValidEmail (email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun EmailInput(
    email: String,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val emailValidity = isValidEmail(email)
    val showError = email.isNotEmpty() && !emailValidity
    val isSubmitEnabled = emailValidity && !isLoading

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Recipient Email") },
            isError = showError,
            placeholder = { Text("Enter the email to receive alerts") },
            modifier = modifier.weight(1f),
            supportingText = {
                if (showError) Text("Please enter valid email")
            }
        )
        Button(
            onClick = onSubmit,
            enabled = isSubmitEnabled,
            modifier = Modifier.height(56.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.width(72.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(24.dp),
//                            .padding(end = 4.dp),
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text("Submit")
                }
            }
        }
    }
}
