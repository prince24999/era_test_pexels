package com.vp.era_test_pexels.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import coil.compose.rememberAsyncImagePainter
import com.vp.era_test_pexels.ui.main.SharedTopBar

class PhotoDetail : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageUrl = intent.getStringExtra("src")
        Log.d("PhotoDetail", "PhotoDetailUrl: $imageUrl")
        setContent {
            var selectedIndex by remember { mutableIntStateOf(0) }

            Scaffold(modifier = Modifier.fillMaxWidth(),
                containerColor = Color.White,

                topBar = {
                    SharedTopBar("Photo Detail")

                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    ZoomableImageScreen(imageUrl.toString())

                }
            }
        }
    }

}

@Composable
fun ZoomableImageScreen(imageUrl: String) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    // container size, we use fillMaxSize() then it's just the screen size
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            // get container size to calculate limit to X (horizontal)
            .onSizeChanged { containerSize = it }
            // double-tap to reset scale and offsets
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        scale = 1f
                        offsetX = 0f
                        offsetY = 0f
                    }
                )
            }
            //  pinch-to-zoom và pan
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    //  scale limit update from 1x to 5x
                    scale = (scale * zoom).coerceIn(1f, 5f)

                    //  fillMaxSize() and scale = 1 then image fit to container,
                    // calculate :
                    // maxOffsetX = (containerWidth * (scale - 1)) / 2
                    val maxOffsetX = if (scale > 1f) (containerSize.width * (scale - 1f)) / 2f else 0f
                    offsetX = (offsetX + pan.x).coerceIn(-maxOffsetX, maxOffsetX)

                    // Y : vertical is free
                    offsetY += pan.y
                }
            }
            //  scale and translation for image
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offsetX,
                translationY = offsetY
            )
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}








