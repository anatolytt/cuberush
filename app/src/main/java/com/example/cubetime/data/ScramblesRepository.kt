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
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.internal.cookieToString

class ScramblesRepository {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val KEEP_GENERATED = 5  // количество скрамблов, которые должны всегда быть наготове

    private val nextScrambles = MutableStateFlow<List<String>>(emptyList())
    val currentScramble = mutableStateOf<String>("")
    var scrambleIsGenerated : Boolean = false
    val currentEvent = mutableStateOf<Events>(Events.CUBE333)
    val currentImage = mutableStateOf<String?>("")

    var job: Job? = null


    fun addScramble(scramble: String) {
        nextScrambles.update { list -> list + scramble }
    }

    private fun keepScramblesGenerated() {
        job = coroutineScope.launch {
            while (nextScrambles.value.size < KEEP_GENERATED) {
                val scramble = Scrambler().generateScramble(currentEvent.value)
                ensureActive()
                addScramble(scramble)
            }
        }

    }

    fun clearScrambles() {
        job?.cancel()
        nextScrambles.update { emptyList() }
    }

    fun updateImage() {
        coroutineScope.launch(Dispatchers.Default) {
            val pictureString = Scrambler().createScramblePicture(
                currentScramble.value,
                currentEvent.value
            )
            currentImage.value = pictureString
        }
    }

    suspend fun updateNextScramble(event: Events) {
        Log.d("ScramblesRepository", event.toString())
        job?.cancel()
        if (currentEvent.value != event) {
            clearScrambles()
            currentEvent.value = event
        }
        keepScramblesGenerated()

        currentScramble.value = "Generating..."
        if (nextScrambles.value.isNotEmpty()) {
            currentScramble.value = nextScrambles.value[0]
            Log.d("Solves", nextScrambles.value.toString())
            nextScrambles.update { list -> list.drop(1) }
            scrambleIsGenerated = false
        } else {
            currentScramble.value = Scrambler().generateScramble(event)
        }
        updateImage()
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