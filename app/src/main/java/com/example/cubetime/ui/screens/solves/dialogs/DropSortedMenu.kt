package com.example.cubetime.ui.screens.solves.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cubetime.ui.screens.solves.SolvesViewModel
import com.example.cubetime.ui.screens.solves.sortState

@Composable
fun DropSortedMenu(solvesViewModel: SolvesViewModel)
{
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Показать меню")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Text("По возрастанию", fontSize=18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        solvesViewModel.isStateSorted.value = sortState.SORTEDSTART
                        solvesViewModel.saveMode()
                    })
            Text("По убванию", fontSize=18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        solvesViewModel.isStateSorted.value = sortState.SORTEDEND
                        solvesViewModel.saveMode()
                    })

            Text("Сброс", fontSize=18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        solvesViewModel.isStateSorted.value = sortState.DEFAULT
                        solvesViewModel.saveMode()
                    })

        }
    }

}