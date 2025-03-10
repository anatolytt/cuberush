package com.example.cubetime.ui.screens.timer.Dialogs

import android.app.Dialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cubetime.R
import com.example.cubetime.model.Events
@Preview(showBackground = true)
@Composable
fun DeletedDialog(

){
    Dialog(
        onDismissRequest = { }
    ) {
        Card(
            modifier = Modifier
                .width(250.dp)
                .height(100.dp)
                .padding(10.dp))
        {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top=10.dp)
            ) {
                Text(text = "Удалить сборку?",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, start = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween


                ) {
                    Button(
                        onClick = {},

                    ) {

                    }
                    Text("BUTTON2")

                }

            }
        }
    }

}