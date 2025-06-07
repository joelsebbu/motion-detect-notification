package com.example.openpalmapp.network

import com.example.openpalmapp.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.lang.Exception

data class EmailSendResult(
    val success: Boolean,
    val emailSent: Boolean,
    val message: String
)

suspend fun sendEmailToApiGateway(email: String): EmailSendResult = withContext(Dispatchers.IO) {
    try {
        val client = OkHttpClient()

        val json = """{"email": "$email"}"""
        val mediaType = "application/json".toMediaType()
        val body = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(BuildConfig.EMAIL_API_URL)
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""
        val jsonResponse = JSONObject(responseBody)

        if(response.isSuccessful) {
            val emailSent = jsonResponse.optBoolean("email_sent", true)
            val message = jsonResponse.optString("message", "Success")

            EmailSendResult(success = true, emailSent = emailSent, message = message)
        } else{
            val errorMsg = jsonResponse.optString("error", "Unknown error occurred")
            EmailSendResult(success = false, emailSent = false, message = errorMsg)
        }

    } catch (e: Exception) {
        e.printStackTrace()
        EmailSendResult(success = false, emailSent = false, message = "Exception: ${e.message}")
    }
}