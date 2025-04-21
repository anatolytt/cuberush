package com.example.cubetime.ui.screens.statistics



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cubetime.utils.TimeFormat

@Preview
@Composable
fun StatCard(
    statName: String = "Ao5",
    result: String = "3.47",
    cardModifier: Modifier = Modifier.height(50.dp).width(170.dp),
    type: StatCardType = StatCardType.SMALL,
    //result: MutableState<Int>
) {
    ElevatedCard(
        modifier = cardModifier

    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            if (type == StatCardType.BIG) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = statName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = result,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Monospace

                    )
                }
            } else if (type == StatCardType.SMALL) {
                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = statName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(end = 15.dp, start = 10.dp),

                    )
                    Text(
                        text = result,
                        fontSize = 22.sp
                    )
                }
            }
        }

    }

}