package com.example.cubetime.ui.screens.statistics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope


import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cubetime.R
import com.example.cubetime.data.model.StatType
import com.example.cubetime.shared.AppStrings
import com.example.cubetime.ui.screens.statistics.dialogs.StatBottomSheet
import com.example.cubetime.ui.shared.SharedViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun StatisticsScreen(
    viewModel: SharedViewModel,
    statisticsViewModel: StatisticsViewModel
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val solvesToShow by statisticsViewModel.solvesToShow.collectAsState()
    val chosenAvgType by statisticsViewModel.chosenAvgType.collectAsState()
    val chosenAvgResult by statisticsViewModel.chosenAvgResult.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val includeScrambles by viewModel.settingsManager.getPrintScrambles().collectAsState(true)

    val scope = rememberCoroutineScope()
    if (solvesToShow != null && chosenAvgType != null && chosenAvgResult != null) {
        StatBottomSheet(
            onDismiss = { scope.launch {
                sheetState.hide()
                statisticsViewModel.clearSolvesToShow()
            } },
            sheetState = sheetState,
            solvesList = solvesToShow!!,
            statType = chosenAvgType!!,
            avgResult = chosenAvgResult!!,
            includeScrambles = includeScrambles,
            getLink = {solves -> viewModel.uploadSolves(solves)}
        )
    }


    val averages by statisticsViewModel.averages.collectAsState(initial = CurrentStatsUI())
    val bestAverages by statisticsViewModel.PBs.collectAsState(initial = CurrentStatsUI())
    val solvesCounter by statisticsViewModel.solvesCounter.collectAsState(initial = 0)
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    StatCard(
                        statName = stringResource(R.string.solves_amount),
                        type = StatCardType.SMALL,
                        result = solvesCounter.toString(),
                        cardModifier = Modifier.height(45.dp).width(170.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName = stringResource(R.string.mean),
                        type = StatCardType.SMALL,
                        result = averages.mean ,
                        cardModifier = Modifier.height(45.dp).width(170.dp).composed {
                            if (averages.mean != AppStrings.emptyResult) {
                                Modifier.clickable {
                                    scope.launch {
                                        statisticsViewModel.setSolvesToShow(StatType.MEAN, false)
                                        sheetState.show()
                                    }
                                }
                            } else Modifier
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    StatCard(
                        statName =
                        stringResource(R.string.current) +
                                " " + stringResource(R.string.mo3),
                        type = StatCardType.BIG,
                        result = averages.mo3,
                        cardModifier = Modifier.height(70.dp).width(170.dp).composed {
                            if (bestAverages.mo3 != AppStrings.emptyResult) {
                                Modifier.clickable {
                                    scope.launch {
                                        statisticsViewModel.setSolvesToShow(StatType.MO3, false)
                                        sheetState.show()
                                    }
                                }
                            } else Modifier
                        }
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName =
                        stringResource(R.string.pb) +
                                " " + stringResource(R.string.mo3),
                        type = StatCardType.BIG,
                        result = bestAverages.mo3,
                        cardModifier = Modifier.height(70.dp).width(170.dp).composed {
                            if (bestAverages.mo3 != AppStrings.emptyResult) {
                                Modifier.clickable {
                                    scope.launch {
                                        statisticsViewModel.setSolvesToShow(StatType.MO3, true)
                                        sheetState.show()
                                    }
                                }
                            } else Modifier
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    StatCard(
                        statName =
                        stringResource(R.string.current) +
                                " " + stringResource(R.string.ao5),
                        type = StatCardType.BIG,
                        result = averages.ao5,
                        cardModifier = Modifier.height(70.dp).width(170.dp).composed {
                            if (averages.ao5 != AppStrings.emptyResult) {
                                Modifier.clickable {
                                    scope.launch {
                                        statisticsViewModel.setSolvesToShow(StatType.AO5, false)
                                        sheetState.show()
                                    }
                                }
                            } else Modifier
                        }
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName =
                        stringResource(R.string.pb) +
                                " " + stringResource(R.string.ao5),
                        type = StatCardType.BIG,
                        result = bestAverages.ao5,
                        cardModifier = Modifier.height(70.dp).width(170.dp).composed {
                            if (bestAverages.ao5 != AppStrings.emptyResult) {
                                Modifier.clickable {
                                    scope.launch {
                                        statisticsViewModel.setSolvesToShow(StatType.AO5, true)
                                        sheetState.show()
                                    }
                                }
                            } else Modifier
                        }
                    )

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    StatCard(
                        statName =
                        stringResource(R.string.current) +
                                " " + stringResource(R.string.ao12),
                        type = StatCardType.BIG,
                        result = averages.ao12,
                        cardModifier = Modifier.height(70.dp).width(170.dp).composed {
                            if (averages.ao12 != AppStrings.emptyResult) {
                                Modifier.clickable {
                                    scope.launch {
                                        statisticsViewModel.setSolvesToShow(StatType.AO12, false)
                                        sheetState.show()
                                    }
                                }
                            } else Modifier
                        }
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName =
                        stringResource(R.string.pb) +
                                " " + stringResource(R.string.ao12),
                        type = StatCardType.BIG,
                        result = bestAverages.ao12,
                        cardModifier = Modifier.height(70.dp).width(170.dp).composed {
                            if (bestAverages.ao12 != AppStrings.emptyResult) {
                                Modifier.clickable {
                                    scope.launch {
                                        statisticsViewModel.setSolvesToShow(StatType.AO12, true)
                                        sheetState.show()
                                    }
                                }
                            } else Modifier
                        }
                    )


                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    StatCard(
                        statName =
                        stringResource(R.string.current) +
                                " " + stringResource(R.string.ao25),
                        type = StatCardType.BIG,
                        result = averages.ao25,
                        cardModifier = Modifier.height(70.dp).width(170.dp).composed {
                            if (averages.ao25 != AppStrings.emptyResult) {
                                Modifier.clickable {
                                    scope.launch {
                                        statisticsViewModel.setSolvesToShow(StatType.AO25, false)
                                        sheetState.show()
                                    }
                                }
                            } else Modifier
                        }
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName =
                        stringResource(R.string.pb) +
                                " " + stringResource(R.string.ao25),
                        type = StatCardType.BIG,
                        result = bestAverages.ao25,
                        cardModifier = Modifier.height(70.dp).width(170.dp).composed {
                            if (bestAverages.ao25 != AppStrings.emptyResult) {
                                Modifier.clickable {
                                    scope.launch {
                                        statisticsViewModel.setSolvesToShow(StatType.AO25, true)
                                        sheetState.show()
                                    }
                                }
                            } else Modifier
                        }
                    )

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    StatCard(
                        statName =
                        stringResource(R.string.current) +
                                " " + stringResource(R.string.ao50),
                        type = StatCardType.BIG,
                        result = averages.ao50,
                        cardModifier = Modifier.height(70.dp).width(170.dp).composed {
                            if (averages.ao50 != AppStrings.emptyResult) {
                                Modifier.clickable {
                                    scope.launch {
                                        statisticsViewModel.setSolvesToShow(StatType.AO50, false)
                                        sheetState.show()
                                    }
                                }
                            } else Modifier
                        }
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName =
                        stringResource(R.string.pb) +
                                " " + stringResource(R.string.ao50),
                        type = StatCardType.BIG,
                        result = bestAverages.ao50,
                        cardModifier = Modifier.height(70.dp).width(170.dp).composed {
                            if (bestAverages.ao50 != AppStrings.emptyResult) {
                                Modifier.clickable {
                                    scope.launch {
                                        statisticsViewModel.setSolvesToShow(StatType.AO50, true)
                                        sheetState.show()
                                    }
                                }
                            } else Modifier
                        }
                    )

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Spacer(modifier = Modifier.width(14.dp))

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    StatCard(
                        statName =
                        stringResource(R.string.current) +
                                " " + stringResource(R.string.ao100),
                        type = StatCardType.BIG,
                        result = averages.ao100,
                        cardModifier = Modifier.height(70.dp).width(170.dp).composed {
                            if (averages.ao100 != AppStrings.emptyResult) {
                                Modifier.clickable {
                                    scope.launch {
                                        statisticsViewModel.setSolvesToShow(StatType.AO100, false)
                                        sheetState.show()
                                    }
                                }
                            } else Modifier
                        }
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName =
                        stringResource(R.string.pb) +
                                " " + stringResource(R.string.ao100),
                        type = StatCardType.BIG,
                        result = bestAverages.ao100,
                        cardModifier = Modifier.height(70.dp).width(170.dp).composed {
                            if (bestAverages.ao100 != AppStrings.emptyResult) {
                                Modifier.clickable {
                                    scope.launch {
                                        statisticsViewModel.setSolvesToShow(StatType.AO100, true)
                                        sheetState.show()
                                    }
                                }
                            } else Modifier
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    StatCard(
                        statName =
                        stringResource(R.string.pb) +
                                " " + stringResource(R.string.single),
                        type = StatCardType.BIG,
                        result = bestAverages.single,
                        cardModifier = Modifier.height(70.dp).width(170.dp).composed {
                            if (bestAverages.single != AppStrings.emptyResult) {
                                Modifier.clickable {
                                    scope.launch {
                                        statisticsViewModel.setSolvesToShow(StatType.SINGLE, true)
                                        sheetState.show()
                                    }
                                }
                            } else Modifier
                        }
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName = "empty",
                        type = StatCardType.BIG,
                        result = "-",
                        cardModifier = Modifier.height(70.dp).width(170.dp)
                    )
                }
            }

        }
    }
}