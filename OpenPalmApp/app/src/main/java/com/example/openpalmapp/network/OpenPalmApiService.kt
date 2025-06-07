package com.example.openpalmapp.network

import com.example.openpalmapp.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

suspend fun sendEmailToApiGateway(email: String): Boolean = withContext(Dispatchers.IO) {
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
        response.isSuccessful
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}