package com.example.cubetime.utils

import org.worldcubeassociation.tnoodle.scrambles.Puzzle
import org.worldcubeassociation.tnoodle.scrambles.PuzzleRegistry
import org.worldcubeassociation.tnoodle.svglite.Color
import org.worldcubeassociation.tnoodle.svglite.Svg

class Scrambler {
    suspend fun generateScramble(event: String) : String {
        val event = getPuzzleRegisteryFromName(event)
        return event?.scrambler?.generateScramble() ?: ""
    }

    suspend fun createScramblePicture(scrambler: String, event: String) : Svg? {
        val event = getPuzzleRegisteryFromName(event)
        val picture : Svg? = event?.scrambler?.drawScramble(scrambler, mutableMapOf())
        return picture
    }

    private fun getPuzzleRegisteryFromName(event : String) : PuzzleRegistry? {
        return when (event) {
            "cube222" -> PuzzleRegistry.TWO
            "cube333" -> PuzzleRegistry.THREE
            "cube444" -> PuzzleRegistry.FOUR
            "cube555" -> PuzzleRegistry.FIVE
            "cube666" -> PuzzleRegistry.SIX
            "cube777" -> PuzzleRegistry.SEVEN
            "clock" -> PuzzleRegistry.CLOCK
            "pyraminx" -> PuzzleRegistry.PYRA
            "skewb" -> PuzzleRegistry.SKEWB
            "megaminx" -> PuzzleRegistry.MEGA
            "sq1" -> PuzzleRegistry.SQ1
            "cube333bf" -> PuzzleRegistry.THREE_NI
            "cube444bf" -> PuzzleRegistry.FOUR_NI
            "cube555bf" -> PuzzleRegistry.FIVE_NI
            else -> null
        }
    }

}

