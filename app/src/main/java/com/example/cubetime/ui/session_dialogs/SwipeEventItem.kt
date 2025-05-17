package com.example.cubetime.ui.session_dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeSessionItem(
    deleteAction: () -> Unit,
    editAction: () -> Unit,
    onExpanded: () -> Unit,
    onCollapsed: () -> Unit,
    isShown: Boolean,
    content: @Composable () -> Unit
) {
    var contextMenuWidth by remember {  // ширина меню (delete и edit)
        mutableStateOf(0f)
    }

    val offset = remember {    // текущий сдвиг контента
        Animatable(initialValue = 0f)
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect (isShown, contextMenuWidth) {
        if (isShown) {
            offset.animateTo(contextMenuWidth)  // полностью раскрываем меню
        } else {
            offset.animateTo(0f)    // полностью скрываем меню
        }
    }

    Box(
        modifier = Modifier
            .widthIn()
            // чтобы высота контента была такой же или выше высоты самого высокого элемента в нем.
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .onSizeChanged {
                    contextMenuWidth = it.width.toFloat()
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ActionItem (
                onClick = {deleteAction()},
                backgroundColor = MaterialTheme.colorScheme.error,
                icon = Icons.Default.Delete
            )
            ActionItem (
                onClick = {editAction()},
                backgroundColor = MaterialTheme.colorScheme.onTertiary,
                icon = Icons.Default.Edit
            )
        }

        Surface (
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .pointerInput(contextMenuWidth) {
                    detectHorizontalDragGestures (
                        onHorizontalDrag = {change, dragAmount ->
                            coroutineScope.launch {
                                val newOffcet = (offset.value + dragAmount)
                                    .coerceIn(0f, contextMenuWidth) // чтобы не свайпалось левее левого
                                                                    // или правее правого края
                                offset.snapTo(newOffcet)
                            }},
                        onDragEnd = {
                            // Если правее середины - раскрываем меню, если левее - скрываем
                            if (offset.value >= contextMenuWidth / 2f) {
                                coroutineScope.launch {
                                    offset.animateTo(contextMenuWidth)
                                    onExpanded()
                                }
                            } else {
                                coroutineScope.launch {
                                    offset.animateTo(0f)
                                    onCollapsed()
                                }

                            }
                        }
                    )
                }
        ) {
            content()
        }
    }

}