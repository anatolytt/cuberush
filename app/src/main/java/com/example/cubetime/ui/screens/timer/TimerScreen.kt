package com.example.cubetime.ui.screens.timer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.utils.Scrambler
import kotlinx.coroutines.coroutineScope
import org.worldcubeassociation.tnoodle.svglite.Svg

@Composable
fun TimerScreen(
    viewModel: SharedViewModel
) {
    ScrambleImage(
        svgString = viewModel.currentImage,
        sizeDp = 250
    )

}



