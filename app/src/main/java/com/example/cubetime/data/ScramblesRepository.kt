package com.example.cubetime.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.cubetime.data.model.Events
import com.example.cubetime.utils.Scrambler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScramblesRepository {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val KEEP_GENERATED = 10  // количество скрамблов, которые должны всегда быть наготове

    private val nextScrambles = MutableStateFlow<List<String>>(emptyList())
    val currentScramble = mutableStateOf<String>("")
    var scrambleIsGenerated : Boolean = false
    val currentEvent = mutableStateOf<Events>(Events.CUBE333)
    val currentImage = mutableStateOf<String?>("")

    var job: Job? = null


    fun addScramble(scramble: String) {
        nextScrambles.update { list -> list + scramble }
    }

    fun addCustomScramble(scramble: String) {
        addScramble(scramble)
        coroutineScope.launch {
            updateNextScramble(currentEvent.value)
        }
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

    suspend fun updateNextScramble(event: Events): Boolean {
        Log.d("ScramblesRepository", event.toString())
        job?.cancel()
        if (currentEvent.value != event) {
            clearScrambles()
            currentEvent.value = event
        }

        keepScramblesGenerated()

        if (nextScrambles.value.isNotEmpty()) {
            currentScramble.value = nextScrambles.value[nextScrambles.value.size-1]
            Log.d("Solves", nextScrambles.value.toString())
            nextScrambles.update { list -> list.dropLast(1) }
            scrambleIsGenerated = false
        } else {
            currentScramble.value = Scrambler().generateScramble(event)
        }
        updateImage()
        return true
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