package com.vp.era_test_pexels.ui


import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import coil.compose.rememberAsyncImagePainter
import com.vp.era_test_pexels.ui.main.SharedTopBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request



class PhotoDetail : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageUrl = intent.getStringExtra("originalUrl")
        Log.d("PhotoDetail", "PhotoDetailUrl: $imageUrl")
        val photographer = intent.getStringExtra("photographer")
        val alt = intent.getStringExtra("alt")

        //Log.d("PhotoDetail", "PhotoDetailUrl: $imageUrl")
        setContent {

            FullScreenEffect() // overlap statusbar

            Scaffold(modifier = Modifier.fillMaxSize(),

            ) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    // 🟢 Hình ảnh (Nằm dưới cùng)
                    ZoomableImageScreen(imageUrl.toString())

                    // 🔵 Footer chứa 2 dòng Text, đè lên dưới cùng
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter) // ✅ Căn ở dưới cùng
                            .background(Color.Transparent)
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = alt.toString(),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic
                            )
                            Text(
                                modifier = Modifier.padding(top = 4.dp),
                                text = photographer.toString(),
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // 🔴 Header chứa Button, đè lên phía trên cùng
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopEnd) // ✅ Căn ở trên cùng
                            .background(Color.Transparent)
                            .padding(8.dp)
                    ) {
                        DownloadBar(imageUrl.toString())
                    }
                }


            }
        }
    }

}

@Composable
fun DownloadBar(imageUrl: String) {
    val coroutineScope = rememberCoroutineScope()
    fun getBitmapFromUrl(imageUrl: String): Bitmap? {
        return try {
            val request = Request.Builder().url(imageUrl).build()
            val response = OkHttpClient().newCall(request).execute()
            BitmapFactory.decodeStream(response.body?.byteStream())
        } catch (e: Exception) {
            Log.e("ImageDownload", "Download error: $e")
            null
        }
    }

    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()

            .background(Color.Transparent)
            .padding(top = 8.dp)
    )
    {
        Button(
            modifier = Modifier
                .border(1.dp, Color.White, RoundedCornerShape(20.dp))
                .background(Color.Transparent, RoundedCornerShape(20.dp))
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ), onClick = {

                coroutineScope.launch {
                    val bitmap = withContext(Dispatchers.IO) { getBitmapFromUrl(imageUrl) }
                    saveImageToGallery(context, bitmap!!, "downloaded_image")
                }

            }) {
            Text(text = "Download")
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
//                    val maxOffsetY = if (scale > 1f) (containerSize.height * (scale - 1f)) / 2f else 0f
//                    offsetY = (offsetY + pan.y).coerceIn(-maxOffsetY, maxOffsetY)
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

fun saveImageToGallery(context: Context, imageBitmap: Bitmap, imageName: String) {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$imageName.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    val contentResolver = context.contentResolver
    val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    imageUri?.let { uri ->
        contentResolver.openOutputStream(uri)?.use { outputStream ->
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
        }
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






