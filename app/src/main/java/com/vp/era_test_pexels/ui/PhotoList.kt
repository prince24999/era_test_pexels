package com.vp.era_test_pexels.ui

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import com.google.gson.Gson
import com.vp.era_test_pexels.model.ApiResponse
import com.vp.era_test_pexels.network.Network


import com.vp.era_test_pexels.ui.main.SharedTopBar
import okhttp3.Request

class PhotoList : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        super.onCreate(savedInstanceState)
        setContent {
            var selectedIndex by remember { mutableIntStateOf(0) }

            Scaffold(modifier = Modifier.fillMaxWidth(),
                containerColor = Color.White,

                topBar = {
                    SharedTopBar("Era Photo Search")

                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    test()

                }
            }
        }
    }
}

@Composable
fun test()
{
    val apiKey = "Y3dDyi8upPyObSyzc6swlKMR0YyGgfLunIoHCvgkXwALQ9cev030eIMQ"


    val context = LocalContext.current
    val a = Network()
    val res: String = a.fetchPexelsPhotos(apiKey, "snow", "landscape", "medium", "black", "en-US", 1, 15)
    if (res != "e")
    {
        //Log.d("Network", res.length.toString())

    }
    else
    {
        //val apiRes: ApiResponse = Gson().fromJson(res, ApiResponse::class.java)
        //Log.e("Network", "Error fetching data")
    }


}