package com.vp.era_test_pexels.network

import android.util.Log
import com.google.gson.Gson
import com.vp.era_test_pexels.control.photoSearchBaseUrl
import com.vp.era_test_pexels.model.ApiResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException


class Network {


        fun fetchPhotos(
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

            //val json = """{"key": "value"}"""
            //val mediaType = "application/json".toMediaType()
            //val body = json.toRequestBody(mediaType)
            val requestUrl: String = photoSearchBaseUrl + "query=$query&orientation=$orientation&size=$size&color=$color&locale=$locale&page=$pageNumber&per_page=$perPage"
            val request = Request.Builder()
                .url(requestUrl)
                .addHeader("Authorization", apiKey)
                .get()
                .build()

            try {
                Log.d("Network", "URL : $requestUrl")
                Log.d("Network", "Fetching data...")
                val response: Response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                //Log.d("Network", responseBody ?: "Response body is null")
                //Log.d("Network", "Response Code: ${response.code}")
                return responseBody.toString()
            } catch (e: IOException) {
                Log.e("Network", "Error fetching data: ${e.message}")
                return "e"
            }

        }



    fun parseApiResponse(jsonString: String): ApiResponse? {
        return try {
            Log.d("Network", "JSON String: $jsonString")
            Gson().fromJson(jsonString, ApiResponse::class.java)
        } catch (e: Exception) {
            println("Lỗi khi parse JSON: ${e.message}")
            null
        }
    }


}