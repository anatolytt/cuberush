package com.example.cubetime.ui.screens.versus.Dialogs

import android.app.Dialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.entities.Solve
import com.example.cubetime.ui.screens.versus.VersusViewModel

@Composable
fun PenaltyVersus(
    onDismissRequest: () -> Unit,
    versusViewModel: VersusViewModel,
) {


    Dialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismissRequest() },
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .graphicsLayer {
                            rotationX = 180f
                            rotationY = 180f
                        },
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            versusViewModel.timer1.changePenalty(Penalties.DNF)
                            versusViewModel.updatePenaltyTop(versusViewModel.repository1.currentSession.value.id,Penalties.DNF)



                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.Black
                        )
                    )
                    {
                        Text("DNF")
                    }
                    Button(onClick = {
                        versusViewModel.timer1.changePenalty(Penalties.PLUS2)
                        versusViewModel.updatePenaltyTop(versusViewModel.repository1.currentSession.value.id,Penalties.PLUS2)
                    },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Yellow,
                            contentColor = Color.Black
                        )
                    )
                    { Text("+2") }
                    Button(onClick = {}) { Text("OK") }

                }
            }
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            versusViewModel.timer2.changePenalty(Penalties.DNF)
                            versusViewModel.updatePenaltyBottom(versusViewModel.repository2.currentSession.value.id,Penalties.DNF)
//                            versusViewModel.timer2.changePenalty(Penalties.DNF)
//                            versusViewModel.updatePenaltyBottom(id = 0,Penalties.DNF)
                             },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.Black
                        )

                    ) { Text("DNF") }
                    Button(onClick = {
                        versusViewModel.timer2.changePenalty(Penalties.PLUS2)
                        versusViewModel.updatePenaltyBottom(versusViewModel.repository2.currentSession.value.id,Penalties.PLUS2)

                                     },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Yellow,
                            contentColor = Color.Black
                        )
                        )
                    { Text("+2") }
                    Button(onClick = {


                    }) { Text("OK") }

                }
            }
        }

    }

}