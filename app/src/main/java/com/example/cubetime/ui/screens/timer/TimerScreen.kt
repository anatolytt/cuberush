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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.utils.Scrambler
import kotlinx.coroutines.coroutineScope
import org.worldcubeassociation.tnoodle.svglite.Svg



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cubetime.R
import com.example.cubetime.model.Events
import com.example.cubetime.utils.Scrambler
import kotlinx.coroutines.launch


@Preview(showBackground = true)
@Composable
fun TimerScreen() {
    val scrambler = Scrambler()
    val event = Events.CUBE333
    var scramble by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()



    LaunchedEffect(event) {
        scramble = scrambler.generateScramble(event)
    }
    //вверех и низ экрана таймера( срамбл и картинга снизу )

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = 20.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            scramble.split("\n").forEach { line ->
                Text(
                    text = line,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,

                    modifier = Modifier
                        .padding(bottom = 5.dp, start = 10.dp, end = 10.dp)
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Row {
                    //карандаш
                    IconButton(onClick = {
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.pencil),
                            contentDescription = "Generate scramble"
                        )
                    }
                    //ввод времени
                    IconButton(onClick = {

                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.timeradd),
                            contentDescription = "Generate scramble"
                        )
                    }
                }
                //веертушка для срамбла
                IconButton(onClick = {
                    coroutineScope.launch {
                        scramble = scrambler.generateScramble(event)
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrowreload),
                        contentDescription = "Generate scramble"
                    )
                }

            }

        Spacer(modifier = Modifier.weight(1f))
        //Низ Экрана таймера СТАТИСТИКА

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, end = 10.dp, start = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    modifier = Modifier.padding(bottom = 5.dp, end = 10.dp, start = 10.dp),
                    elevation = CardDefaults.elevatedCardElevation(5.dp),
                    shape = CardDefaults.shape
                ) {
                    Text(text = "Разброс:8.77\nСреднее:12 \nЛучшее:12\nК-во:21",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 7.dp, top = 7.dp, end = 7.dp, start = 7.dp))
                }

                    ScrambleImage(
                        svgString = viewModel.currentImage,
                        sizeDp = 100
                    )

                Card(
                    modifier = Modifier.padding(bottom = 5.dp, end = 10.dp, start = 10.dp),
                    shape = CardDefaults.shape,
                    elevation = CardDefaults.elevatedCardElevation(5.dp)
                ) {
                    Text(text = "Ao5: 2.37\nAo12: 19.32\nAo50: 2.37\nAo100: ...",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 7.dp, top = 7.dp, end = 7.dp, start = 7.dp))
                }
            }



    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center

        ) {
            Timer()
        }
    //все кнопки под временем 0.00
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top=150.dp),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 5.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            //кнопка удаления результата
            IconButton(onClick = {
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.cross),
                    contentDescription = "Generate scramble"
                )
            }
            //кнопка вернуть скрамбл
            IconButton(onClick = {

            }) {
                Icon(
                    painter = painterResource(id = R.drawable.reload),
                    contentDescription = "Generate scramble"
                )
            }
            //кнопка для dnf
            IconButton(onClick = {

            }) {
                Icon(
                    painter = painterResource(id = R.drawable.dnf),
                    contentDescription = "Generate scramble"
                )
            }
            //кнопка добавление +2 ко времени
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.plustwo),
                    contentDescription = "Generate scramble"
                )
            }
            //кнопка добавления коментария сборки
            IconButton(onClick = {}
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.commentadd),
                    contentDescription = "Generate scramble"
                )
            }


        }

    }
}



