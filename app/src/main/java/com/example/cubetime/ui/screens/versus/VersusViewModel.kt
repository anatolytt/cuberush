package com.example.cubetime.ui.screens.versus

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.data.ScramblesRepository
import com.example.cubetime.data.local.AppDatabase
import com.example.cubetime.data.local.SolvesRepository
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.ShortSolve
import com.example.cubetime.data.model.entities.Session
import com.example.cubetime.data.model.entities.Solve
import com.example.cubetime.ui.screens.settings.Settings
import com.example.cubetime.ui.screens.timer.TimerController
import com.example.cubetime.ui.screens.timer.TimerState
import com.example.cubetime.utils.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

class VersusViewModel : ViewModel() {
    lateinit var repository1: SolvesRepository
    lateinit var repository2: SolvesRepository
    lateinit var scramblesRepository: ScramblesRepository

    private var _currentScramble = mutableStateOf<String>("")
    val currentScramble get() = _currentScramble.value

    var currentSolves: MutableMap<Int, Pair<Int, Penalties>?> = mutableMapOf(1 to null, 2 to null)

    var solvesBack: MutableMap<Int, Pair<Int, Penalties>?> = mutableMapOf(1 to null, 2 to null)

    private var _counterTop = mutableStateOf<Int>(0)
    val counterTop get() = _counterTop.value

    private var _counterBottom = mutableStateOf<Int>(0)
    val counterBottom get() = _counterBottom.value


    //счет файта
    private var _scoreTop = mutableStateOf<Int>(0)
    val scoreTop get() = _scoreTop.value

    private var _scoreBottom = mutableStateOf<Int>(0)
    val scoreBottom get() = _scoreBottom.value

    private var roundScores = mutableMapOf<Int, Int>()//очки зак каждый раунд


    //обнулить счет при запуске режима
    fun zeroScore() {
        _scoreTop.value = 0
        _scoreBottom.value = 0
    }

    fun addCounterTop() {
        _counterTop.value += 1
    }

    fun addCounterBottom() {
        _counterBottom.value += 1
    }

    fun zeroCounter() {
        _counterTop.value = 0
        _counterBottom.value = 0
    }


    fun init() {
        scramblesRepository = ScramblesRepository.getInstance()
        _currentScramble = scramblesRepository.currentScramble
        updateCurrentScramble()
    }

    fun updateCurrentScramble() {
        viewModelScope.launch(Dispatchers.IO) {
            scramblesRepository.updateNextScramble(repository1.currentSession.value.event)
        }
    }

    private var settings = mutableStateOf(Settings(false, false, false))

    private val _timer1 = mutableStateOf(TimerController(
        generateScr = {},
        addSolve = { time, penalty ->
            solvesBack[1] = Pair(time, Penalties.NONE)
            currentSolves[1] = Pair(time, Penalties.NONE)
            addSolves()
        },
        settings = settings,
        hideEverything = { }
    )
    )
    val timer1 get() = _timer1.value

    private val _timer2 = mutableStateOf(TimerController(generateScr = {

    }, addSolve = { time, penalty ->
        solvesBack[2] = Pair(time, Penalties.NONE)
        currentSolves[2] = Pair(time, Penalties.NONE)
        addSolves()
    }, settings = settings, hideEverything = { }))
    val timer2 get() = _timer2.value

    init {
        val db = AppDatabase.getInstance()
        val dao = db.SolvesDao()
        repository1 = SolvesRepository(dao)
        repository2 = SolvesRepository(dao)
    }

    fun setSessions(session1: Session, session2: Session) = viewModelScope.launch(Dispatchers.IO) {
        repository1.updateCurrentSessionById(session1.id)
        repository2.updateCurrentSessionById(session2.id)
    }



    fun chnageScorePenalty() {
        if (roundScores.isNotEmpty()) {
            val lastRoundNum = roundScores.keys.max()

            val winLastRound = roundScores[lastRoundNum]
            if (winLastRound == 1)
            {
                _scoreTop.value = max(0, _scoreTop.value - 1)
            }
            else if (winLastRound == 2)
            {
                _scoreBottom.value = max(0, _scoreBottom.value - 1)
            }
            roundScores.remove(lastRoundNum)
        }
        changeScore()
    }

    fun changeScore() {

        solvesBack[1] = solvesBack[1]?.copy(second = timer1.penaltyState)
        solvesBack[2] = solvesBack[2]?.copy(second = timer2.penaltyState)
        var solveTop = ShortSolve(
            id = 0,
            solvesBack[1]!!.first,
            solvesBack[1]!!.second
        )
        var solveBot = ShortSolve(
            id = 0,
            solvesBack[2]!!.first,
            solvesBack[2]!!.second
        )

        val resultTop = TimeFormat.solveToResult(solveTop)
        val resultBottom = TimeFormat.solveToResult(solveBot)

//        Log.d("счет раунда ", "${roundScores.size + 1}: $resultTop $resultBottom")

        if (resultTop < resultBottom) {
            _scoreTop.value += 1
            roundScores[roundScores.size] = 1 // top выиграл

//            Log.d("обновленный счет", " ${_scoreTop.value}:${_scoreBottom.value}")
        } else if (resultTop > resultBottom) {
            _scoreBottom.value += 1
            roundScores[roundScores.size] = 2 // bottom победил

//            Log.d("обновленный счет", "${_scoreTop.value}:${_scoreBottom.value}")
        } else {
            roundScores[roundScores.size] = 0 // ничтя
//            Log.d("обновленный счет", "${_scoreTop.value}:${_scoreBottom.value}")
        }
    }

    fun addSolves() {
        if (currentSolves[1] != null && currentSolves[2] != null) {
            repository1.addSolve(
                Solve(
                    id = 0,
                    sessionId = repository1.currentSession.value.id,
                    result = currentSolves[1]!!.first,
                    event = repository1.currentSession.value.event,
                    penalties = currentSolves[1]!!.second,
                    //currentSolves[1]!!.second
                    date = "",
                    scramble = _currentScramble.value,
                    comment = "",
                    reconstruction = "",
                    isCustomScramble = false
                )
            )


            repository2.addSolve(
                Solve(
                    id = 0,
                    sessionId = repository2.currentSession.value.id,
                    result = currentSolves[2]!!.first,
                    event = repository2.currentSession.value.event,
                    penalties = currentSolves[2]!!.second,
                    date = "",
                    scramble = _currentScramble.value,
                    comment = "",
                    reconstruction = "",
                    isCustomScramble = false
                )
            )


            currentSolves[1] = null
            currentSolves[2] = null

            Log.d("Score", solvesBack.toString())

            updateCurrentScramble()
            changeScore()

        }
    }

    fun updateTimerSettings(settings: Settings) {
        this.settings.value = settings
    }

    fun updatePenaltyTop(id: Int, new: Penalties) =
        repository1.updatePenalty(id, new, lastSolve = true)

    fun updatePenaltyBottom(id: Int, new: Penalties) =
        repository2.updatePenalty(id, new, lastSolve = true)


}