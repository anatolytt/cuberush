package com.example.cubetime.ui.shared

import androidx.collection.intIntMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.model.Events
import com.example.cubetime.model.Session
import com.example.cubetime.utils.Scrambler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel : ViewModel() {
    private val _currentEvent = mutableStateOf<Events>(Events.CUBE333)
    val currentEvent : Events get() = _currentEvent.value

    fun setEvent(event: Events) {
        _currentEvent.value = event
        generateNewScrambleAndImage()

    }


    private val currentSession = mutableStateOf<Session>(Session(
        name = "MainSession",
        event = Events.CUBE333,
        createDate = null,
        id = null
    ))

    private val _currentScramble = mutableStateOf<String>("")
    val currentScramble: String get() = _currentScramble.value

    private val _currentImage = mutableStateOf<String?>("")
    val currentImage : String? get() = _currentImage.value

    fun generateNewScrambleAndImage () {
        viewModelScope.launch (Dispatchers.Default){
            val scramble = Scrambler().generateScramble(currentEvent)
            _currentScramble.value = scramble
            val image = Scrambler().createScramblePicture(currentScramble, currentEvent)
            _currentImage.value = image
        }
    }
}

