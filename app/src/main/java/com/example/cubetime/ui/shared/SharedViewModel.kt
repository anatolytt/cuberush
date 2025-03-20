package com.example.cubetime.ui.shared

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.model.Events
import com.example.cubetime.model.Session
import com.example.cubetime.ui.screens.timer.TimerController
import com.example.cubetime.ui.settings.SettingsDataManager
import com.example.cubetime.ui.settings.TimerSettings
import com.example.cubetime.utils.Scrambler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Timer

class SharedViewModel : ViewModel() {
    val KEEP_GENERATED = 5  // количество скрамблов, которые должны всегда быть наготове

    val _settingsScreenOpen = mutableStateOf(false)
    val settingsScreenOpen get() = _settingsScreenOpen.value

    private val _currentSessionID = mutableStateOf(0)   // Пока id будет индекс в массиве, потом исправим
    val currentSessionID get() = _currentSessionID.value

    private val _sessions = mutableStateListOf<Session>(            // Потом добавим получение сессий из БД
            Session(name = "MAIN", event = Events.CUBE333),


    )


    val sessions get() = _sessions.toList()
    val currentSession get() = sessions[currentSessionID]

    private val nextScrambles = ArrayDeque<String>()
    private val _currentScramble = mutableStateOf<String>("")
    val currentScramble get() = _currentScramble.value

    private val _currentImage = mutableStateOf<String?>("")
    val currentImage get() = _currentImage.value

    private val _everythingHidden = mutableStateOf(false)
    val everythingHidden get() = _everythingHidden.value

    private var timerSettings = mutableStateOf(TimerSettings(false, false, false))
    private val _timer = mutableStateOf<TimerController>(TimerController(
        hideEverything = { hide -> hideEverything(hide) },
        generateScr = { updateScramble() },
        settings = timerSettings)
    )
    val timer get() = _timer.value

    //для выбора головоломок при созднии сессии ( состояние выбора)
    private val _selectedEvent = mutableStateOf<Events?>(null)
    val selectedEvent get() = _selectedEvent.value



    fun changeSettingsVisibility () {
        _settingsScreenOpen.value = !_settingsScreenOpen.value
    }

    fun updateTimerSettings(timerSettingsState: TimerSettings) {
        timerSettings.value = timerSettingsState
    }

    fun inputScramble(scramble: String) {
        nextScrambles.add(scramble)
        updateScramble()
    }

    fun updateScramble() {
        generateScrambles()
        viewModelScope.launch (Dispatchers.Default) {
            withContext(Dispatchers.Main) { _currentScramble.value = "Generating..." }
            while (true) {
                if (nextScrambles.size > 0) {
                    withContext(Dispatchers.Main) {
                        _currentScramble.value = nextScrambles.removeLast()
                    }
                    updateImage()
                    break
                }
            }
        }
    }

    fun updateImage() {
        viewModelScope.launch (Dispatchers.Default) {
            val pictureString = Scrambler().createScramblePicture(
                _currentScramble.value,
                currentSession.event)

            withContext(Dispatchers.Main) {
                _currentImage.value = pictureString
            }
        }
    }

    fun generateScrambles() {
        viewModelScope.launch (Dispatchers.Default) {
            while (nextScrambles.size < KEEP_GENERATED) {
                val scramble = Scrambler().generateScramble(currentSession.event)
                withContext(Dispatchers.Main) {
                    nextScrambles.add(scramble)
                }
            }
        }
    }


    fun deleteLastSolve() {    // потом добавим номер сборки в параметры функции для удаления из БД
        timer.stopAndDelete() // потом поменяем
    }

    fun switchSessions(sessionId : Int) {
        _currentSessionID.value = sessionId
        nextScrambles.clear()
        updateScramble()
    }

    fun hideEverything(hide: Boolean) {
        _everythingHidden.value = hide
    }


    //функция Создание сесcии
    fun creatSession(name:String, events: Events){
        val newSession = Session(name, events)
        _sessions.add(newSession)
    }
//    fun selectEvent(event: Events) {
//        _selectedEvent.value = event
//    }
}

