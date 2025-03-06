package com.example.cubetime.ui.appbar.dialogs



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.cubetime.R


@Preview (showBackground = true)

@Composable
fun PuzzleChoice() {
    val listOfPuzzle = listOf(
        R.drawable.img_1 ,
        R.drawable.img_1,
        R.drawable.img_1,
        R.drawable.img_1,
        R.drawable.img_1,
        R.drawable.img_1,
        R.drawable.img_1,
        R.drawable.img_1,
        R.drawable.img_1,
        R.drawable.img_1,
        R.drawable.img_1,
        R.drawable.img_1,
        R.drawable.img_1,
        R.drawable.img_1,
    )


    Dialog(
        onDismissRequest = { }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(30.dp),)
        {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(20.dp) ,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)

            )
            {
                items(listOfPuzzle.size) { index ->
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .padding(1.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = listOfPuzzle[index]),
                            contentDescription = " $index"
                        )

                    }
                }


            }


            }
        }


    }



