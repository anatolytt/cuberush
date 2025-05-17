package com.example.cubetime.ui.screens.shared_solves

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cubetime.R
import com.example.cubetime.ui.screens.solves.dialogs.MyDivider
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.ui.shared.SolveColumnItem
import com.example.cubetime.utils.TimeFormat
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@Composable
fun SharedSolvesScreen (
    sharedViewModel: SharedViewModel,
    sharedSolvesViewModel: SharedSolvesViewModel,
    navController: NavController,
    solvesToken: String = "21022a2b-26eb-4704-a436-c996f783bf16"
) {

    val isError by sharedSolvesViewModel.isError.collectAsState()
    val isLoading by sharedSolvesViewModel.isLoading.collectAsState()
    val solvesList by sharedSolvesViewModel.solves.collectAsState()

    LaunchedEffect (Unit) {
        sharedSolvesViewModel.updateSolves(token = solvesToken)
    }

    BackHandler {
        navController.popBackStack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.solves_amount) + " " + solvesList.size,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        MyDivider(padding = 10.dp)

        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
            )
        }
        if (isError) {
            Text(
                text = stringResource(R.string.error_occured),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        LazyColumn {
            itemsIndexed(
                TimeFormat.solveListToStringAverageList(solvesList)
            ) { index, solve ->
                SolveColumnItem(solve, index+1)
                Spacer(modifier = Modifier.height(2.dp))
            }
        }

        if (isError) {
            OutlinedButton (
                onClick = {
                sharedSolvesViewModel.updateSolves(token = solvesToken)
                          },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 10.dp)
                    .background(MaterialTheme.colorScheme.error)
            ){
                Text(stringResource(R.string.try_again))
            }
        } else if (!isLoading) {
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 10.dp)
            ) {
                Text("Сохр")
            }
        }



    }
}


