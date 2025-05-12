package com.vp.era_test_pexels.ui

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import coil.compose.rememberAsyncImagePainter
import com.vp.era_test_pexels.ui.main.SharedTopBar

class PhotoDetail : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageUrl = intent.getStringExtra("originalUrl")
        val photographer = intent.getStringExtra("photographer")
        val alt = intent.getStringExtra("alt")

        Log.d("PhotoDetail", "PhotoDetailUrl: $imageUrl")
        setContent {

            FullScreenEffect() // overlap statusbar

            Scaffold(modifier = Modifier.fillMaxSize(),

            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {

                    ZoomableImageScreen(imageUrl.toString())

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(8.dp)
                    ) {
                        Column(modifier = Modifier.align(Alignment.CenterStart)) {
                            Text(
                                text = alt.toString(),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic
                            )

                            Text(modifier = Modifier.padding(top = 4.dp),
                                text = photographer.toString(),
                                color = Color.White,
                                fontSize = 12.sp,

                                )
                        }
                    }
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


                    // calculate : limit X (horizontal) to prevent image overflow

                    val maxOffsetX = if (scale > 1f) (containerSize.width * (scale - 1f)) / 2f else 0f
                    offsetX = (offsetX + pan.x).coerceIn(-maxOffsetX, maxOffsetX)

                    // calculate : limit Y (vertical) to prevent image overflow
                    val maxOffsetY = if (scale > 1f) (containerSize.height * (scale - 1f)) / 2f else 0f
                    offsetY = (offsetY + pan.y).coerceIn(-maxOffsetY, maxOffsetY)
                    //offsetY += pan.y
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

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun FullScreenEffect() {
    val window = (LocalActivity.current as Activity).window
    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.insetsController?.apply {
            hide(WindowInsets.Type.statusBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}






