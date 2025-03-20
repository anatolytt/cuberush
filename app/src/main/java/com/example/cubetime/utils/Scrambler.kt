package com.example.cubetime.utils

import com.example.cubetime.model.Events
import org.worldcubeassociation.tnoodle.scrambles.InvalidScrambleException
import org.worldcubeassociation.tnoodle.scrambles.Puzzle
import org.worldcubeassociation.tnoodle.scrambles.PuzzleRegistry
import org.worldcubeassociation.tnoodle.svglite.Color
import org.worldcubeassociation.tnoodle.svglite.Svg

class Scrambler {
    suspend fun generateScramble(event: Events) : String {
        val eventPuzzleRegistry = event.getPuzzleRegistry()
        return eventPuzzleRegistry.scrambler.generateScramble()
    }

    suspend fun createScramblePicture(scramble: String, event: Events) : String? {
        val eventPuzzleRegistry = event.getPuzzleRegistry()
        var imageString: String? = null
        try {
            imageString =
                eventPuzzleRegistry.scrambler.drawScramble(scramble, mutableMapOf())?.toString()
        } catch (_: InvalidScrambleException) {
            imageString = ""
        }
        return imageString
    }
}

