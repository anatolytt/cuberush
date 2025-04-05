package com.example.cubetime.ui.shared


import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.example.cubetime.R
import com.example.cubetime.data.model.Penalties

@Composable
fun PenaltiesDropdownMenu(setPenalty: (Penalties) -> Unit, iconSize: Dp) {
    var expanded by remember { mutableStateOf(false) }
    var penalty by remember { mutableStateOf(Penalties.NONE) }

    IconButton(onClick = { expanded = !expanded }) {
        Icon(painter = (
                if (penalty != Penalties.NONE)
                    painterResource(R.drawable.filledflag)
                else
                    painterResource(R.drawable.flag)
                ),
            contentDescription = "Penalties dropdown menu",
            modifier = Modifier.size(iconSize)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Penalties.entries.forEach { p ->
                DropdownMenuItem(
                    text = { Text(stringResource(p.stringResId)) },
                    onClick = {
                        penalty = p
                        setPenalty(penalty)
                        expanded = false
                    }
                )
            }
        }

    }


}