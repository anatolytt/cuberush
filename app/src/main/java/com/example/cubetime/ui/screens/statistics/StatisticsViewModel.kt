package com.example.cubetime.ui.screens.statistics

import androidx.lifecycle.ViewModel
import com.example.cubetime.data.local.AppDatabase
import com.example.cubetime.data.local.SolvesRepository
import com.example.cubetime.data.model.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class StatisticsViewModel : ViewModel() {
    var bestSingle = MutableStateFlow<Int>(value = 0)
    var single = MutableStateFlow<Int>(value = 0)
    var bestMo3 = MutableStateFlow<Int>(value = 0)
    var mo3 = MutableStateFlow<Int>(value = 0)
    var bestAo5 = MutableStateFlow<Int>(value = 0)
    var ao5 = MutableStateFlow<Int>(value = 0)
    var bestAo12 = MutableStateFlow<Int>(value = 0)
    var ao12 = MutableStateFlow<Int>(value = 0)
    var bestAo50 = MutableStateFlow<Int>(value = 0)
    var ao50 = MutableStateFlow<Int>(value = 0)
    var bestAo25 = MutableStateFlow<Int>(value = 0)
    var ao25 = MutableStateFlow<Int>(value = 0)
    var bestAo100 = MutableStateFlow<Int>(value = 0)
    var ao100 = MutableStateFlow<Int>(value = 0)
    var ao1000 = MutableStateFlow<Int>(value = 0)
    var bestAo1000 = MutableStateFlow<Int>(value = 0)
    var solvesAmount = MutableStateFlow<Int>(value = 0)
    var sessionAverage = MutableStateFlow<Int>(value = 0)

    lateinit var repository : SolvesRepository

    init {
        val db = AppDatabase.getInstance()
        val dao = db.SolvesDao()
        repository = SolvesRepository.getInstance(dao)
    }

}