import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.cubetime.R
import com.example.cubetime.data.model.StatType
import com.example.cubetime.data.model.entities.Solve
import com.example.cubetime.utils.ShareAndCopy
import kotlinx.coroutines.launch

@Preview
@Composable
fun ShareDialog(
    onDismiss: () -> Unit = {},
    solves: List<Solve> = emptyList(),
    statType: StatType = StatType.MEAN,
    getLink: suspend () -> String? = {"abc"},
    result: String = "",
    includeScrambles: Boolean = true
) {
    val context = LocalContext.current
    val sharer = ShareAndCopy()
    val coroutineScope = rememberCoroutineScope()
    val copied = stringResource(R.string.link_copied)

    Dialog(onDismissRequest = onDismiss) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(9.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.share),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 14.dp)
                )

                Row() {
                    FilledTonalButton(
                        onClick = {
                            coroutineScope.launch {
                                sharer.copyString(
                                    context,
                                    getLink() ?: "abc"
                                )

                                Toast.makeText(
                                    context,
                                    copied,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        },
                        modifier = Modifier.width(140.dp).height(55.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = android.R.drawable.ic_menu_search),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(stringResource(R.string.copy_link))
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(15.dp))

                    FilledTonalButton(
                        onClick = {
                            sharer.shareAverage(
                                solves = solves,
                                includeScrambles = includeScrambles,
                                context = context,
                                statType = statType,
                                avgResult = result
                            )
                        },
                        modifier = Modifier.width(140.dp).height(55.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = android.R.drawable.ic_menu_share),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(stringResource(R.string.share))
                        }
                    }
                }
            }
        }
    }
}