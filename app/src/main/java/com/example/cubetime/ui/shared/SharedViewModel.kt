package com.example.cubetime.ui.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.model.Events
import com.example.cubetime.model.Penalties
import com.example.cubetime.model.Session
import com.example.cubetime.model.Solve
import com.example.cubetime.ui.screens.timer.TimerController
import com.example.cubetime.ui.settings.TimerSettings
import com.example.cubetime.utils.Scrambler
import com.example.cubetime.utils.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel : ViewModel() {
    val KEEP_GENERATED = 5  // количество скрамблов, которые должны всегда быть наготове

    val _settingsScreenOpen = mutableStateOf(false)
    val settingsScreenOpen get() = _settingsScreenOpen.value

    private val _currentSessionID =
        mutableStateOf(0)   // Пока id будет индекс в массиве, потом исправим
    val currentSessionID get() = _currentSessionID.value

    private val _sessions = mutableStateListOf<Session>(
        // Потом добавим получение сессий из БД
        Session(name = "MAIN", event = Events.CUBE333),
    )

    private val _solves = mutableStateListOf<Solve>(

    )
    val solve get() = _solves.toList()

    val sessions get() = _sessions.toList()
    val currentSession get() = sessions[currentSessionID]

    private var nextScrambles by mutableStateOf(ArrayDeque<String>())

    private val _currentScramble = mutableStateOf<String>("")
    val currentScramble get() = _currentScramble.value

    private val _currentImage = mutableStateOf<String?>("")
    val currentImage get() = _currentImage.value

    private val _everythingHidden = mutableStateOf(false)
    val everythingHidden get() = _everythingHidden.value

    private var timerSettings = mutableStateOf(TimerSettings(false, false, false))
    private val _timer = mutableStateOf<TimerController>(
        TimerController(
            hideEverything = { hide -> hideEverything(hide) },
            generateScr = { updateScramble() },
            settings = timerSettings,
            addSolve =  {time, penalty -> addToSolveList(time, penalty)}
        )
    )
    val timer get() = _timer.value

    //для выбора головоломок при созднии сессии (состояние выбора)
    private val _selectedEvent = mutableStateOf<Events?>(null)
    val selectedEvent get() = _selectedEvent.value

    private var scramblesGeneratingJob by mutableStateOf<Job?>(null)
    private var scramblesUpdateJob by mutableStateOf<Job?>(null)


    fun changeSettingsVisibility() {
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
        scramblesUpdateJob = viewModelScope.launch(Dispatchers.Default) {
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
        viewModelScope.launch(Dispatchers.Default) {
            val pictureString = Scrambler().createScramblePicture(
                _currentScramble.value,
                currentSession.event
            )

            withContext(Dispatchers.Main) {
                _currentImage.value = pictureString
            }
        }
    }

    fun generateScrambles() {
        scramblesGeneratingJob = viewModelScope.launch(Dispatchers.Default) {
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

    fun switchSessions(sessionId: Int) {
        nextScrambles = ArrayDeque<String>()
        scramblesGeneratingJob?.cancel()
        scramblesUpdateJob?.cancel()
        _currentSessionID.value = sessionId
        updateScramble()
    }

    fun hideEverything(hide: Boolean) {
        _everythingHidden.value = hide
    }


    //функция Создание сесcии
    fun createSession(name: String, events: Events) {
        val newSession = Session(name, events)
        _sessions.add(newSession)
    }

    fun deleteSession(session: Session) {
        val idToDelete = _sessions.indexOf(session)
        if (idToDelete <= _currentSessionID.value) {
            _currentSessionID.value -= 1
        }
        _sessions.remove(session)
    }

    fun renameSession(session: Session, index: Int, newName: String) {
        val newSession = session.copy(name = newName)
        _sessions[index] = newSession
    }

    fun addToSolveList(time: Int, penalty: Penalties){
        _solves.add(Solve(
            result = time,
            event = Events.CUBE333,
            penalties = penalty,
            date = "27.02.2025",
            scramble = currentScramble,
            comment = "",
            reconstruction = "",
            isCustomScramble = false
        ))
    }



}

