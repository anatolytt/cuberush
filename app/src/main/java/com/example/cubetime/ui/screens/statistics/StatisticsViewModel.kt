package com.example.cubetime.ui.screens.statistics

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cubetime.data.local.AppDatabase
import com.example.cubetime.data.local.SolvesRepository
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.Session
import com.example.cubetime.utils.TimeFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class StatisticsViewModel : ViewModel() {
    lateinit var repository : SolvesRepository
    var bestSingle = mutableStateOf(0)
    var single = MutableStateFlow<Int>(value = 0)

    lateinit private var _averages: MutableMap<Int, Int>
    val averages: Map<Int, String> get() = _averages.mapValues { (_, solve) ->
        TimeFormat.millisToString(solve, Penalties.NONE)
    }

    lateinit private var _bestAverages: MutableMap<Int, Int>
    val bestAverages: Map<Int, String> get() = _bestAverages.mapValues { (_, solve) ->
        TimeFormat.millisToString(solve, Penalties.NONE)
    }


    init {
        val db = AppDatabase.getInstance()
        val dao = db.SolvesDao()
        repository = SolvesRepository.getInstance(dao)
        _bestAverages = repository.bestAverages
        _averages = repository.averages
    }

}