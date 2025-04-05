package com.example.cubetime.ui.screens.solves

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cubetime.ui.shared.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(viewModel: SharedViewModel){
    TopAppBar(
        title = { Text("Выбрано элементов") },
        navigationIcon = {
            IconButton(onClick = {
                //viewModel.disableDeleteMode()
            }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "выйти из режима выбора"
                )
            }
        },
        actions = {
            IconButton(onClick = {
             //   viewModel.deleteSelectedSolves()
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "удалить выбранное"
                )
            }
        }
    )



}