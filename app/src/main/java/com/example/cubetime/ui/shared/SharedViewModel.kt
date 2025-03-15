package com.example.cubetime.ui.shared

import android.util.Log
import androidx.collection.intIntMapOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.R
import com.example.cubetime.model.Events
import com.example.cubetime.model.Session
import com.example.cubetime.utils.Scrambler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel : ViewModel() {
    private val _currentSessionID = mutableStateOf(0)   // Пока id будет индекс в массиве, потом исправим
    val currentSessionID get() = _currentSessionID.value

    val _sessions = mutableStateListOf<Session>(            // Потом добавим получение сессий из БД
            Session(name = "MAIN", event = Events.SQ1),
            Session(name = "333 2h",  event = Events.CUBE333),
            Session(name = "my 555 session", event = Events.CUBE555),
            Session(name = "best event", event = Events.CLOCK),
            Session(name = "worfsdklajl", event = Events.SQ1),
            Session(name = "pyra", event = Events.PYRA),
            Session(name = "скуб", event = Events.SKEWB),
            Session(name = "скван", event = Events.SQ1),
            Session(name = "какое-то название", event = Events.CUBE222),
            Session(name = "аофлд", event = Events.SQ1),
            Session(name = "фалыщзс", event = Events.CUBE777)
    )
    val sessions get() = _sessions.toList()
    val currentSession get() = sessions[currentSessionID]

    private val _currentScramble = mutableStateOf<String>("")
    val currentScramble get() = _currentScramble.value

    private val _currentImage = mutableStateOf<String?>("")
    val currentImage get() = _currentImage.value

    private val _everythingHidden = mutableStateOf(false)
    val everythingHidden get() = _everythingHidden.value


    fun generateNewScrambleAndImage () {
        _currentScramble.value = "Generating..."
        viewModelScope.launch (Dispatchers.Default){
            val scramble = Scrambler().generateScramble(currentSession.event)
            _currentScramble.value = scramble
            val image = Scrambler().createScramblePicture(currentScramble, currentSession.event)
            _currentImage.value = image
        }
    }

    fun switchSessions(sessionId : Int) {
        _currentSessionID.value = sessionId
        generateNewScrambleAndImage()
    }

    fun hideEverything(hide: Boolean) {
        _everythingHidden.value = hide
    }
}

