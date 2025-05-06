package com.vp.era_test_pexels.ui

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.vp.era_test_pexels.control.apiKey
import com.vp.era_test_pexels.control.calculateOrientationAndSizeOfPhoto

import com.vp.era_test_pexels.network.Network


import com.vp.era_test_pexels.ui.main.SharedTopBar


class PhotoList : ComponentActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        super.onCreate(savedInstanceState)
        setContent {
            // get String
            val query: String? = intent.getStringExtra("query")
            val orientation: String? = intent.getStringExtra("orientation")
            val size: String? = intent.getStringExtra("size")
            val color: String? = intent.getStringExtra("color")
            val locale: String? = intent.getStringExtra("locale")

            // get int, set default if null
            val pageNumber: Int = intent.getIntExtra("pageNumber", 1)
            val perPage: Int = intent.getIntExtra("perPage", 15)

            Log.d("PhotoList", "onCreate: $query $orientation $size $color $locale $pageNumber $perPage")

            var selectedIndex by remember { mutableIntStateOf(0) }

            Scaffold(modifier = Modifier.fillMaxWidth(),
                containerColor = Color.White,

                topBar = {
                    SharedTopBar("Photo List")

                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    GalleryGrid(
                        query = query.toString(),
                        orientation = orientation.toString(),
                        size = size.toString(),
                        color = color.toString(),
                        locale = locale.toString(),
                        pageNumber = pageNumber,
                        perPage = perPage
                    )

                }
            }
        }
    }
}

@Composable
fun PhotoItem(photographer: String, alt: String,imageUrl: String, originalUrl: String, modifier: Modifier = Modifier) {
    Log.d("PhotoList", "PhotoItemUrl: $imageUrl")
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.padding(8.dp)
            .clickable {
                Log.d("PhotoList", "PhotoItemClicked: $imageUrl")

                val intent = Intent(context, PhotoDetail::class.java).apply()
                {
                    putExtra("src", imageUrl)
                    putExtra("originalUrl", originalUrl)
                    putExtra("photographer", photographer)
                    putExtra("alt", alt)
                }
                context.startActivity(intent)
            }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            // Footer on top of Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    Text(
                        text = alt,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic
                    )

                    Text(modifier = Modifier.padding(top = 4.dp),
                        text = photographer,
                        color = Color.White,
                        fontSize = 12.sp,

                    )
                }

            }
        }
    }
}

@Composable
fun GalleryGrid(query: String, orientation: String, size: String, color: String, locale: String, pageNumber: Int, perPage: Int)
{
    val net = Network()
    val jsonResponse: String = net.fetchPhotos(apiKey, query, orientation, size, color, locale, pageNumber, perPage)
    if (jsonResponse != "e")
    {
        Log.d("PhotoList", jsonResponse)
        val apiResponse = net.parseApiResponse(jsonResponse)
        if (apiResponse != null) {
            Log.d("PhotoList", "${apiResponse.photos.size}")

            val photos = apiResponse.photos

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 300.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                items(photos.count()) { index ->
                    PhotoItem(photos[index].photographer,photos[index].alt,calculateOrientationAndSizeOfPhoto(photos[index],orientation, size), photos[index].src.original)
                }
            }
        }
        else
        {
            Log.d("PhotoList", "Error Parsing data")
        }
    }



}