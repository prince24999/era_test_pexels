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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh

import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.vp.era_test_pexels.control.apiKey
import com.vp.era_test_pexels.control.calculateOrientationAndSizeOfPhoto
import com.vp.era_test_pexels.model.Photo

import com.vp.era_test_pexels.network.Network


import com.vp.era_test_pexels.ui.main.SharedTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import com.vp.era_test_pexels.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.coroutineScope


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


            Scaffold(modifier = Modifier.fillMaxWidth(),
                //containerColor = Color.White,

                topBar = {
                    SharedTopBar("Photo List")

                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(color = Color.Blue),
                    contentAlignment = Alignment.TopCenter
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
                Log.d("PhotoList", "PhotoItemClicked: $originalUrl")

                try {
                    val intent = Intent(context, PhotoDetail::class.java).apply()
                    {
                        putExtra("originalUrl", originalUrl)
                        putExtra("photographer", photographer)
                        putExtra("alt", alt)
                    }
                    context.startActivity(intent)
                }
                catch (e: Exception)
                {
                    Log.e("PhotoList", "PhotoItemClicked: $e")
                }

            }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {

                Image(
                    painter = rememberAsyncImagePainter(
                        model = imageUrl,
                        placeholder = ColorPainter(Color.Gray),

                    ),
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

//@Composable
//fun GalleryGrid(query: String, orientation: String, size: String, color: String, locale: String, pageNumber: Int, perPage: Int)
//{
//    var nextPageUrl by remember { mutableStateOf("") }
//
//    val net = Network()
//    val jsonResponse: String = net.fetchPhotos(apiKey, query, orientation, size, color, locale, pageNumber, perPage)
//    if (jsonResponse != "e")
//    {
//        Log.d("PhotoList", jsonResponse)
//        val apiResponse = net.parseApiResponse(jsonResponse)
//        if (apiResponse != null) {
//            Log.d("PhotoList", "${apiResponse.photos.size}")
//
//            val photos = apiResponse.photos
//            nextPageUrl = apiResponse.nextPage.toString()
//
//            LazyVerticalGrid(
//                columns = GridCells.Adaptive(minSize = 300.dp),
//                modifier = Modifier.padding(8.dp)
//            ) {
//                items(photos.count()) { index ->
//                    PhotoItem(photos[index].photographer,photos[index].alt,calculateOrientationAndSizeOfPhoto(photos[index],orientation, size), photos[index].src.original)
//                }
//            }
//        }
//        else
//        {
//            Log.d("PhotoList", "Error Parsing data")
//        }
//    }
//
//}

@Composable
fun GalleryGrid(
    query: String,
    orientation: String,
    size: String,
    color: String,
    locale: String,
    pageNumber: Int,
    perPage: Int
) {
    val context = LocalContext.current
    var nextPageUrl by rememberSaveable { mutableStateOf<String?>(null) }
    var photos by rememberSaveable { mutableStateOf<List<Photo>>(emptyList()) }
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var isLoadNew by rememberSaveable { mutableStateOf(true) }
    var isLoadNext by rememberSaveable { mutableStateOf(true) }
    val listState = rememberLazyGridState()
    val net = Network()

    suspend fun fetchAndParseToPhotos()
    {
        if(isLoading) {
            val jsonResponse = net.fetchPhotos(
                apiKey,
                query,
                orientation,
                size,
                color,
                locale,
                pageNumber,
                perPage
            )

            if (jsonResponse != "e") {
                val apiResponse = net.parseApiResponse(jsonResponse)
                if (apiResponse != null) {
                    photos = apiResponse.photos

                    Log.d("PhotoList", "New photos size: ${photos.size}")

                    nextPageUrl = if (apiResponse.nextPage.isNullOrEmpty()) {
                        "1"
                    } else {
                        apiResponse.nextPage.toString()
                    }

                }
            }
            delay(700)
            isLoading = false
        }
    }

    suspend fun fetchAndParseToPhotosNextPage()
    {
        if(isLoading) {
            val jsonResponse = net.fetchPhotosNextPage(nextPageUrl.toString())
            if (jsonResponse != "e") {
                val apiResponse = net.parseApiResponse(jsonResponse)
                if (apiResponse != null) {
                    photos = photos + apiResponse.photos
                    Log.d("PhotoList", "New photos size: ${photos.size}")
                    nextPageUrl = if (apiResponse.nextPage.isNullOrEmpty()) {
                        "1"
                    } else {
                        apiResponse.nextPage.toString()
                    }

                }
            }


            delay(700)
            isLoading = false
        }
    }

    LaunchedEffect(isLoadNew)
    {
        //Log.d("PhotoListLaunchedEffect", "LaunchedEffect - isLoadNew: $isLoadNew")
        if(isLoadNew  && isLoading)
        {
            Log.d("PhotoListLaunchedEffect", "Fetching data")

            photos = emptyList()
            fetchAndParseToPhotos()
            isLoading = false
            isLoadNew = false
        }
        //Log.d("PhotoListLaunchedEffect", "LaunchedEffect -  isLoadNew: $isLoadNew")
    }


    // watching scroll state to automatically load more data
    LaunchedEffect(listState) {
        if (photos.isNotEmpty()) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                .map { visibleItems ->
                    val lastVisibleItem = visibleItems.lastOrNull()?.index ?: 0
                    val totalItems = photos.size

                    (lastVisibleItem > totalItems - 2)


                // Check if reach end of List - 2 items
                }
                .distinctUntilChanged()
                .collect { shouldLoadMore ->
                    if ((shouldLoadMore) && (nextPageUrl != "1") && !isLoading ) {
                        isLoading = true
                        fetchAndParseToPhotosNextPage()
                        isLoading = false
                    }
                }
        }
    }

    // Refresh Button
    Scaffold(
        floatingActionButton = {
            val coroutineScope = rememberCoroutineScope()
            FloatingActionButton(
                containerColor = Color.DarkGray,
                contentColor = Color.White,
                onClick = {
                    coroutineScope.launch {
                        listState.scrollToItem(0)
                    }
                    isLoadNew = true
                    isLoading = true
                    photos = emptyList()
                    nextPageUrl = null
                    Log.d("PhotoListLaunchedEffect", "FAB - Load new data")
                }
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (isLoading)
            {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    //CircularProgressIndicator(color = Color.White)
                    Log.d("LaunchedEffect", "isLoading - $isLoading")
                    Log.d("LaunchedEffect", "Show progress  bar")
                    LinearProgressIndicator()

                }
            }
            else if (photos.isEmpty())
            {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Photos Found",
                        modifier = Modifier.align(Alignment.Center).padding(16.dp),
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }
            }
            else {
//                if(photos.isNotEmpty())
//                {
                    // Grid of photos
                    LazyVerticalGrid(
                        state = listState,
                        columns = GridCells.Adaptive(minSize = 300.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        items(photos.count()) { index ->

                            PhotoItem(
                                photos[index].photographer,
                                photos[index].alt,
                                calculateOrientationAndSizeOfPhoto(photos[index], orientation, size),
                                photos[index].src.original
                            )
                        }

                    }
//                }
//                else
//                {
//
//                }

            }



        }
    }
}





