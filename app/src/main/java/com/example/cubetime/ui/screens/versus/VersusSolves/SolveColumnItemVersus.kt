package com.example.cubetime.ui.screens.versus.VersusSolves

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cubetime.data.model.ShortSolve
import com.example.cubetime.data.model.entities.Solve
import com.example.cubetime.ui.screens.versus.VersusViewModel
import com.example.cubetime.utils.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SolveColumnItemVersus(
    solve: ShortSolve,
    solveNumber: Int,
    versusViewModel: VersusViewModel,
    numPlayer: Int
) {
//    val solveWithScramble = remember(solve.id) {
//        if (numPlayer == 1) {
//            versusViewModel.repository1.getSolveById(solve.id)
//        } else {
//            versusViewModel.repository2.getSolveById(solve.id)
//        }
//    }
    var solveScramble by remember { mutableStateOf<Solve?>(null) }

    LaunchedEffect(solve.id, numPlayer) {
        withContext(Dispatchers.IO) {
            solveScramble = if (numPlayer == 1) {
                versusViewModel.repository1.getSolveById(solve.id)
            } else {
                versusViewModel.repository2.getSolveById(solve.id)
            }
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),

        ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$solveNumber",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = TimeFormat.millisToString(solve.result, solve.penalties),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            solveScramble?.let {
                Text(
                    text = it.scramble,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

}