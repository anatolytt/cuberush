package com.example.cubetime.ui.screens.timer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Composable
fun ScrambleImage(svgString: String?, sizeDp: Int) {
    val context = LocalContext.current

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }

    var imageData by remember { mutableStateOf<ByteArray?>(null) }

    LaunchedEffect(svgString) {
        imageData = svgString?.toByteArray()
    }

    if (imageData != null) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context)
                    .data(imageData)
                    .build(),
                imageLoader
            ),
            contentDescription = "scramble image",
            modifier = Modifier.size(sizeDp.dp),
            contentScale = ContentScale.Fit
        )
    } else {
        // Плейсхолдер, пока изображение загружается
        Box(
            modifier = Modifier
                .size(sizeDp.dp)
                .background(Color.LightGray)
        ) {
            Text(
                text = "Loading...",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }
    }
}