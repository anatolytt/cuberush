package com.example.cubetime.data

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.cubetime.data.local.SolvesDao
import com.example.cubetime.data.local.SolvesRepository
import com.example.cubetime.data.model.Events
import com.example.cubetime.utils.Scrambler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.internal.cookieToString

class ScramblesRepository {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val mutex = Mutex()
    val KEEP_GENERATED = 5  // количество скрамблов, которые должны всегда быть наготове

    private val nextScrambles = MutableStateFlow<List<String>>(emptyList())
    val currentScramble = mutableStateOf<String>("")
    var scrambleIsGenerated : Boolean = false

    var job: Job? = null


    fun addScramble(scramble: String) {
        nextScrambles.update { list -> list + scramble }
    }

    private fun keepScramblesGenerated(event: Events) {
        job = coroutineScope.launch {
            while (nextScrambles.value.size < KEEP_GENERATED) {
                val scramble = Scrambler().generateScramble(event)
                addScramble(scramble)
            }
        }

    }

    fun clearScrambles() {
        job?.cancel()
        nextScrambles.update { emptyList() }
    }

    suspend fun updateNextScramble(event: Events) {
        Log.d("Scrams", "Вызвано")
        job?.cancel()
        keepScramblesGenerated(event)
        coroutineScope.launch {
            currentScramble.value = "Generating..."
            if (nextScrambles.value.isNotEmpty()) {
                currentScramble.value = nextScrambles.value[0]
                Log.d("Solves", nextScrambles.value.toString())
                nextScrambles.update { list -> list.drop(1) }
                scrambleIsGenerated = false
            } else {
                currentScramble.value = Scrambler().generateScramble(event)
            }

        }


    }
    companion object {
        private var INSTANCE: ScramblesRepository? = null

        fun getInstance(): ScramblesRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = ScramblesRepository()
                }
                INSTANCE = instance
                return  instance
            }
        }
    }



}