package com.example.cubetime.ui.screens.solves.dialogs

import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cubetime.R
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.Solve
import com.example.cubetime.ui.screens.solves.SolvesViewModel
import com.example.cubetime.ui.screens.timer.TimerScreen
import com.example.cubetime.ui.screens.timer.dialogs.CommentDialog
import com.example.cubetime.ui.shared.ScrambleImage
import com.example.cubetime.ui.shared.SharedViewModel
import com.example.cubetime.ui.theme.outlineDark
import com.example.cubetime.utils.Scrambler
import com.example.cubetime.utils.Sharing
import com.example.cubetime.utils.TimeFormat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolveBottomSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState,
    solve: Solve,
    solvesViewModel: SolvesViewModel
) {
    var scrambleImageString by remember { mutableStateOf<String?>("") }
    LaunchedEffect (Unit) {
        scrambleImageString = Scrambler().createScramblePicture(
            scramble = solve.scramble,
            event = solve.event
        )
    }

    var penalty by remember { mutableStateOf(solve.penalties) }
    val commentState = remember { mutableStateOf(solve.comment) }

    var showCommentDialog by remember { mutableStateOf(false) }

    if (showCommentDialog) {
        CommentDialog(
            onDismiss = { showCommentDialog = false },
            action = {newComment ->
                commentState.value = newComment
                solvesViewModel.updateComment(solve.id, newComment)
                     },
            initialComment = commentState.value
        )
    }
    val sharer = Sharing()
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss

    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ){
            Column (
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        horizontal = 5.dp
                    )
                ) {
                    Text(
                        text = "Time: " + TimeFormat.millisToString(
                            millis = solve.result,
                            penalty = penalty
                        ),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.weight(1f)

                    )
                    Text(
                        text = solve.date,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Medium
                    )
                }

                MyDivider(padding = 12.dp)

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                ) {
                    Icon(
                        painter = painterResource(solve.event.getIconDrawableId()),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 12.dp),
                    )
                    Text(
                        text = stringResource(solve.event.getEventStringId()),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    )

                }

                MyDivider(padding = 12.dp)

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    Column (
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 15.dp)
                    ) {
                        Text(
                            text = solve.scramble,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp)
                        ) {
                            ScrambleImage(
                                svgString = scrambleImageString,
                                sizeDp = 230.dp,
                            )
                        }

                        Row() {
                            Spacer(modifier = Modifier.weight(1f))
                            PenaltyButton(
                                action = {
                                    penalty = Penalties.NONE
                                    solvesViewModel.updatePenalty(solve.id, Penalties.NONE)
                                         },
                                text = stringResource(R.string.ok),
                                painter = painterResource(id = R.drawable.cross),
                                selected = (penalty == Penalties.NONE),
                                modifier = Modifier
                            )
                            PenaltyButton(
                                action = {
                                    penalty = Penalties.PLUS2
                                    solvesViewModel.updatePenalty(solve.id, Penalties.PLUS2)
                                         },
                                text = stringResource(R.string.plus_two),
                                painter = painterResource(id = R.drawable.plustwo),
                                selected = (penalty == Penalties.PLUS2),
                                modifier = Modifier.padding(start = 6.dp)
                            )
                            PenaltyButton(
                                action = {
                                    penalty = Penalties.DNF
                                    solvesViewModel.updatePenalty(solve.id, Penalties.DNF)
                                         },
                                text = stringResource(R.string.dnf),
                                painter = painterResource(id = R.drawable.dnf),
                                selected = penalty == Penalties.DNF,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                }

                MyDivider(8.dp)

                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            solvesViewModel.deleteSolve(solve.id)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text(
                            stringResource(R.string.delete),
                        )
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }

                    FilledTonalButton(
                        onClick = {
                            sharer.shareSolve(solve, context)
                        },
                        modifier = Modifier.padding(start=6.dp)
                    ) {
                        Text(
                            stringResource(R.string.share),
                        )
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }

                MyDivider(padding = 6.dp)

                Card (
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showCommentDialog = true
                        }
                ) {
                    Column (
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 15.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.comment),
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Monospace,
                        )

                        Box (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .border(1.dp, OutlinedTextFieldDefaults.colors().focusedPlaceholderColor),
                        ) {
                            Text(
                                text = commentState.value,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                }

            }

        }


    }

}

@Composable
fun MyDivider(padding: Dp) {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = padding)
    )
}

@Composable
fun PenaltyButton(
    action: () -> Unit,
    text: String,
    painter: Painter,
    selected: Boolean,
    modifier: Modifier
    ) {
    OutlinedButton(
        onClick = action,
        contentPadding = PaddingValues(
            horizontal = 10.dp,
            vertical = 1.5.dp
        ),
        modifier = modifier.widthIn(100.dp).height(40.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                Color.Transparent
            },
            contentColor = if (selected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text)
        }
    }
}