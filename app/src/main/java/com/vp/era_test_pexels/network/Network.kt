package com.vp.era_test_pexels.network

import android.util.Log
import com.google.gson.Gson
import com.vp.era_test_pexels.model.ApiResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class Network {


        fun fetchPexelsPhotos(
            apiKey: String,
            query: String,
            orientation: String,
            size: String,
            color: String,
            locale: String,
            pageNumber: Int,
            perPage: Int
        ): String {
            val client = OkHttpClient()

            val json = """{"key": "value"}"""
            val mediaType = "application/json".toMediaType()
            val body = json.toRequestBody(mediaType)

            val request = Request.Builder()
                .url("https://api.pexels.com/v1/search?query=$query&orientation=landscape&size=&color=&locale=&page=1&per_page=2")
                .addHeader("Authorization", apiKey)
                .get()
                .build()

            try {
                val response: Response = client.newCall(request).execute()
                Log.d("Network", "Response Code: ${response.code}")
                return response.body
            } catch (e: IOException) {
                Log.e("Network", "Error fetching data: ${e.message}")
                return "e"
            }

        }



}