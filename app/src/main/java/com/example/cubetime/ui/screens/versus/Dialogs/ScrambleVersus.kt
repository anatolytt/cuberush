package com.example.cubetime.ui.screens.versus.Dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog

@Composable
fun ScrambleVersus(
    onDismissRequest: ()  -> Unit
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
                modifier = Modifier.fillMaxWidth()

            ) {

                Text("B' R2 F32 B22 L32 B22 U32 R3' D3 R2 B22 U22 R22 L32 B3' D3 R3' B2 U2 F2 B' U2 F32 B32 U32 F L3' U2 F32 B22 L2 U2 F22 L3 R32 F3' R2' D32 R D2 F32 R2 B2 L32 B2 U2 F U32 R2 F R3' D2' L2 U3' L2 F2' D2 B2 F2 D' R2 B2 D22 U2' R22 B32 D3' F3' D3 R' B2' F22 R' D2' R' F22 D R2 D2 R2'")

            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {


                Text("B' R2 F32 B22 L32 B22 U32 R3' D3 R2 B22 U22 R22 L32 B3' D3 R3' B2 U2 F2 B' U2 F32 B32 U32 F L3' U2 F32 B22 L2 U2 F22 L3 R32 F3' R2' D32 R D2 F32 R2 B2 L32 B2 U2 F U32 R2 F R3' D2' L2 U3' L2 F2' D2 B2 F2 D' R2 B2 D22 U2' R22 B32 D3' F3' D3 R' B2' F22 R' D2' R' F22 D R2 D2 R2'")

            }
        }

    }
}