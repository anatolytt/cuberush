package com.example.cubetime.ui.appbar.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.cubetime.R
import com.example.cubetime.model.Events
import com.example.cubetime.model.EventDialog

@Composable
fun EventDialog(
    onDismiss : () -> Unit,
    onBack:()->Unit,
    puzzleIcon: List<EventDialog> =
        listOf(
              EventDialog(nameEvents = Events.CUBE222, event = Events.CUBE222),
              EventDialog(nameEvents = Events.CUBE333,event = Events.CUBE333),
              EventDialog(nameEvents = Events.CUBE444,event = Events.CUBE444),
              EventDialog(nameEvents = Events.CUBE555,event = Events.CUBE555),
              EventDialog(nameEvents = Events.CUBE666,event = Events.CUBE666),
              EventDialog(nameEvents = Events.CUBE777,event = Events.CUBE777),
              EventDialog(nameEvents = Events.ONE_HANDED,event = Events.ONE_HANDED),
              EventDialog(nameEvents = Events.PYRA,event = Events.PYRA),
              EventDialog(nameEvents = Events.SKEWB,event = Events.SKEWB),
              EventDialog(nameEvents = Events.SQ1,event = Events.SQ1),
              EventDialog(nameEvents = Events.MEGA,event = Events.MEGA),
              EventDialog(nameEvents = Events.BF333,event = Events.BF333),
              EventDialog(nameEvents = Events.BF444,event = Events.BF444),
              EventDialog(nameEvents = Events.BF555,event = Events.BF555),
              EventDialog(nameEvents = Events.MBLD,event = Events.MBLD),
        )
) {
    Dialog(
        onDismissRequest = {onDismiss() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(525.dp)
                .padding(10.dp),)
        {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(15.dp) ,
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)

            )
            {
                items(puzzleIcon.size) { index ->
                    val puzzleIcon = puzzleIcon[index]

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .padding(1.dp)
                                .fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(puzzleIcon.event.getIconDrawableId()),
                                contentDescription = " $index"
                            )
                        }
                        Text(
                            text = "${puzzleIcon.nameEvents}",
                            )
                    }

                }


            }
           Row(
               modifier = Modifier.fillMaxSize(),
               horizontalArrangement = Arrangement.Center
           ) {
               Button(
                   onClick = { onBack() },
               ) {
                   Text(
                       text = stringResource(R.string.back_to_session)
                   )
               }
           }

                



            }
        }


    }



