package com.example.cubetime.ui.screens.statistics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubetime.data.local.AppDatabase
import com.example.cubetime.data.SolvesRepository
import com.example.cubetime.data.model.Events
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.Solve
import com.example.cubetime.data.model.Stat
import com.example.cubetime.data.model.StatType
import com.example.cubetime.utils.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatisticsViewModel : ViewModel() {
    private var repository : SolvesRepository

    private val _averages = MutableStateFlow(CurrentStatsUI())
    val averages = _averages.asStateFlow()

    private val _PBs = MutableStateFlow(CurrentStatsUI())
    val PBs = _PBs.asStateFlow()

    private val _solvesCounter = MutableStateFlow<Int>(0)
    val solvesCounter = _solvesCounter.asStateFlow()

    init {
        val db = AppDatabase.getInstance()
        val dao = db.SolvesDao()

        repository = SolvesRepository.getInstance(dao)
        viewModelScope.launch (Dispatchers.Default) {
            repository.currentAverages.collect { newAverages ->
                _averages.update { TimeFormat.mapToUIStats(newAverages) }
            }
        }

        viewModelScope.launch (Dispatchers.Default) {
            repository.bestAverages.collect { newAverages ->
                _PBs.update { TimeFormat.mapToUIStats(newAverages) }
            }
        }

        viewModelScope.launch (Dispatchers.Default) {
            repository.solvesCounter.collect { newCounter ->
                _solvesCounter.update { newCounter }
            }
        }
    }

    private val _solvesToShow = MutableStateFlow<List<Solve>?>(null)
    val solvesToShow = _solvesToShow.asStateFlow()
    private val _chosenAvgType = MutableStateFlow<StatType?>(null)
    val chosenAvgType = _chosenAvgType.asStateFlow()
    private val _chosenAvgResult = MutableStateFlow<String?>(null)
    val chosenAvgResult = _chosenAvgResult.asStateFlow()

    fun setSolvesToShow(statType: StatType, isBest: Boolean) = viewModelScope.launch {
        _solvesToShow.value = repository.getAverageSolves(statType, isBest)
        _chosenAvgType.value = statType
        _chosenAvgResult.value = (if (isBest) PBs else averages).value.getStat(statType)
    }

    fun clearSolvesToShow() {
        _solvesToShow.value = null
        _chosenAvgType.value = null
        _chosenAvgResult.value = null
    }


}