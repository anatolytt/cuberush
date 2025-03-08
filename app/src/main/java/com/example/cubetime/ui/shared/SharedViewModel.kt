package com.example.cubetime.ui.shared

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cubetime.model.Events

class SharedViewModel : ViewModel() {
    private val _currentEvent = mutableStateOf<Events>(Events.CUBE333)
    private val currentSession = mutableStateOf<String?>(null)

    val currentEvent : Events
        get() = _currentEvent.value
}