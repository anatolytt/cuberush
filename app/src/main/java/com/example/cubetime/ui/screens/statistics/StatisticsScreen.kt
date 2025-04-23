package com.example.cubetime.ui.screens.statistics

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cubetime.R
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.utils.TimeFormat


@Composable

fun StatisticsScreen(
    viewModel: SharedViewModel,
    statisticsViewModel: StatisticsViewModel
) {
    val averages : Map<Int, String> = statisticsViewModel.averages
    val bestAverages: Map<Int, String> = statisticsViewModel.bestAverages

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
                        result = "231",
                        cardModifier = Modifier.height(45.dp).width(170.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName = stringResource(R.string.mean),
                        type = StatCardType.SMALL,
                        result = "12.10",
                        cardModifier = Modifier.height(45.dp).width(170.dp)
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
                        result = "231",
                        cardModifier = Modifier.height(70.dp).width(170.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName =
                        stringResource(R.string.pb) +
                        " " + stringResource(R.string.mo3),
                        type = StatCardType.BIG,
                        result = "12.10",
                        cardModifier = Modifier.height(70.dp).width(170.dp)
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
                                " " + stringResource(R.string.ao5),
                        type = StatCardType.BIG,
                        result = averages.get(5)!!,
                        cardModifier = Modifier.height(70.dp).width(170.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName =
                        stringResource(R.string.pb) +
                                " " + stringResource(R.string.ao12),
                        type = StatCardType.BIG,
                        result = "12.10",
                        cardModifier = Modifier.height(70.dp).width(170.dp)
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
                                " " + stringResource(R.string.ao25),
                        type = StatCardType.BIG,
                        result = "231",
                        cardModifier = Modifier.height(70.dp).width(170.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName =
                        stringResource(R.string.pb) +
                                " " + stringResource(R.string.ao50),
                        type = StatCardType.BIG,
                        result = "12.10",
                        cardModifier = Modifier.height(70.dp).width(170.dp)
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
                                " " + stringResource(R.string.ao100),
                        type = StatCardType.BIG,
                        result = "231",
                        cardModifier = Modifier.height(70.dp).width(170.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName =
                        stringResource(R.string.pb) +
                                " " + stringResource(R.string.ao1000),
                        type = StatCardType.BIG,
                        result = "12.10",
                        cardModifier = Modifier.height(70.dp).width(170.dp)
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
                        result = "231",
                        cardModifier = Modifier.height(70.dp).width(170.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName =
                        stringResource(R.string.current) +
                                " " + stringResource(R.string.ao5),
                        type = StatCardType.BIG,
                        result = "12.10",
                        cardModifier = Modifier.height(70.dp).width(170.dp)
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
                        result = "231",
                        cardModifier = Modifier.height(70.dp).width(170.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName =
                        stringResource(R.string.current) +
                                " " + stringResource(R.string.ao25),
                        type = StatCardType.BIG,
                        result = "12.10",
                        cardModifier = Modifier.height(70.dp).width(170.dp)
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
                        result = "231",
                        cardModifier = Modifier.height(70.dp).width(170.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    StatCard(
                        statName =
                        stringResource(R.string.current) +
                                " " + stringResource(R.string.ao100),
                        type = StatCardType.BIG,
                        result = "12.10",
                        cardModifier = Modifier.height(70.dp).width(170.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    StatCard(
                        statName =
                        stringResource(R.string.current) +
                                " " + stringResource(R.string.ao1000),
                        type = StatCardType.BIG,
                        result = "231",
                        cardModifier = Modifier.height(70.dp).width(170.dp)
                    )
                }
            }

        }
    }
}