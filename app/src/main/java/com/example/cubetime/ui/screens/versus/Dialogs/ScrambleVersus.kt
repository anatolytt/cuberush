package com.example.cubetime.ui.screens.versus.Dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.cubetime.ui.screens.versus.VersusViewModel

@Composable
fun ScrambleVersus(
    onDismissRequest: ()  -> Unit,
    versusViewModel: VersusViewModel
)
{
    Dialog(
        onDismissRequest = {
            onDismissRequest()
        },
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onDismissRequest()
                },
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Card(
                modifier = Modifier.fillMaxWidth().graphicsLayer {
                    rotationX = 180f
                    rotationY = 180f
                }

            ) {
                Text(modifier = Modifier.padding(10.dp),
                    text = versusViewModel.currentScramble
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(modifier = Modifier.padding(10.dp),
                    text = versusViewModel.currentScramble
                )


            }
        }

    }
}