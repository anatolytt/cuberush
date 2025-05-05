package com.example.cubetime.ui.screens.statistics.dialogs

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cubetime.R
import com.example.cubetime.data.model.entities.Solve
import com.example.cubetime.data.model.StatType
import com.example.cubetime.ui.screens.solves.dialogs.MyDivider
import com.example.cubetime.utils.ShareAndCopy
import com.example.cubetime.utils.TimeFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatBottomSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState,
    solvesList: List<Solve>,
    statType: StatType,
    avgResult: String,
    includeScrambles: Boolean
) {

    val shareAndCopy = ShareAndCopy()
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column {
                Row() {
                    Text(
                        text = "${statType.getName()}: $avgResult",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp).clickable {
                            shareAndCopy.shareAverage(
                                solves = solvesList,
                                includeScrambles = includeScrambles,
                                context = context,
                                statType = statType,
                                avgResult = avgResult
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.Default.AccountBox,  // Copy icon
                        contentDescription = null,
                        modifier = Modifier.size(35.dp).clickable {
                            shareAndCopy.copyAverage(
                                solves = solvesList,
                                includeScrambles = includeScrambles,
                                context = context,
                                statType = statType,
                                avgResult = avgResult
                            )
                            Toast.makeText(
                                context,
                                "$avgResult ${statType.getName().lowercase()} ${context.getString(R.string.copied)}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )

                }
                MyDivider(padding = 10.dp)

                LazyColumn() {
                    itemsIndexed(
                        TimeFormat.solveListToStringAverageList(solvesList, statType)
                    ) { index, solve ->
                        SolveColumnItem(solve, index+1)
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }

            }

        }
    }

}