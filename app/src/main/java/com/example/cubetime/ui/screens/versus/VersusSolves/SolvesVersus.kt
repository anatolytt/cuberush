package com.example.cubetime.ui.screens.versus.VersusSolves
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cubetime.R
import com.example.cubetime.data.model.entities.Solve
import com.example.cubetime.ui.screens.versus.VersusViewModel
import com.example.cubetime.utils.TimeFormat

@Composable
fun SolvesVersus(
    versusViewModel: VersusViewModel,
    navController: NavController
)
{
    val solves1 by versusViewModel.repository1.shortSolves.collectAsState(initial = emptyList())
    val solves2 by versusViewModel.repository2.shortSolves.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.weight(1f).padding(5.dp)
            .graphicsLayer {
                rotationX = 180f
                rotationY = 180f
            }) {
            LazyColumn(modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(solves1) { index, shortSolve ->
                    SolveColumnItemVersus(
                        solve = shortSolve,
                        solveNumber = index + 1,
                        versusViewModel,
                        numPlayer = 1
                    )

                }
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = Color.Gray
        )
        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center

            ){
            Button(onClick = {
                navController.popBackStack(
                    route = "versus",
                    inclusive = false
                )
            }) {
                Text(stringResource(R.string.back_to_session))
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = Color.Gray
        )
        Column(modifier = Modifier.weight(1f).padding(5.dp)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(solves2) { index, shortSolve ->
                    SolveColumnItemVersus(
                        solve = shortSolve,
                        solveNumber = index + 1,
                        versusViewModel,
                        numPlayer = 2
                    )

                }
            }
        }
    }
}

