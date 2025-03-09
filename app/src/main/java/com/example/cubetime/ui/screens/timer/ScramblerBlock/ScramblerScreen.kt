import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cubetime.R
import com.example.cubetime.model.Events
import com.example.cubetime.utils.Scrambler
import kotlinx.coroutines.launch

@Composable
fun ScramblerScreen(scrambler: Scrambler, event: Events) {
    var scramble by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(event) {
        scramble = scrambler.generateScramble(event)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = scramble,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(onClick = {
                coroutineScope.launch {
                    scramble = scrambler.generateScramble(event)
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.img_1),
                    contentDescription = "Generate scramble"
                )
            }
        }
    }
}
