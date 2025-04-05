package com.example.cubetime.data.model

import android.content.Context
import android.graphics.drawable.VectorDrawable
import androidx.compose.ui.res.painterResource
import com.example.cubetime.R
import org.worldcubeassociation.tnoodle.scrambles.PuzzleRegistry

enum class Events {
    CUBE222,
    CUBE333,
    CUBE444,
    CUBE555,
    CUBE666,
    CUBE777,
    ONE_HANDED,
    PYRA,
    SKEWB,
    SQ1,
    CLOCK,
    MEGA,
    BF333,
    BF444,
    BF555,
    MBLD;

    fun getEventStringId() : Int{
        return when(this) {
            CUBE222 -> R.string.cube222
            CUBE333 -> R.string.cube333
            CUBE444 -> R.string.cube444
            CUBE555 -> R.string.cube555
            CUBE666 ->  R.string.cube666
            CUBE777 ->  R.string.cube777
            ONE_HANDED ->  R.string.oh
            PYRA ->  R.string.pyra
            SKEWB -> R.string.skewb
            SQ1 -> R.string.sq1
            CLOCK -> R.string.clock
            MEGA -> R.string.mega
            BF333 -> R.string.cube333bf
            BF444 ->  R.string.cube444bf
            BF555 ->  R.string.cube555bf
            MBLD -> R.string.multibld
        }
    }


    fun getPuzzleRegistry() : PuzzleRegistry {
        return when(this) {
            CUBE222 -> PuzzleRegistry.TWO
            CUBE333 -> PuzzleRegistry.THREE
            CUBE444 -> PuzzleRegistry.FOUR
            CUBE555 -> PuzzleRegistry.FIVE
            CUBE666 ->  PuzzleRegistry.SIX
            CUBE777 ->  PuzzleRegistry.SEVEN
            ONE_HANDED ->  PuzzleRegistry.THREE
            PYRA ->  PuzzleRegistry.PYRA
            SKEWB -> PuzzleRegistry.SKEWB
            SQ1 -> PuzzleRegistry.SQ1
            CLOCK -> PuzzleRegistry.CLOCK
            MEGA -> PuzzleRegistry.MEGA
            BF333 -> PuzzleRegistry.THREE_NI
            BF444 ->  PuzzleRegistry.FOUR_NI
            BF555 ->  PuzzleRegistry.FIVE_NI
            MBLD -> PuzzleRegistry.THREE
        }
    }

    fun getIconDrawableId(): Int {
        return when(this) {
            CUBE222 -> R.drawable._222
            CUBE333 -> R.drawable._333
            CUBE444 -> R.drawable._444
            CUBE555 -> R.drawable._555
            CUBE666 ->  R.drawable._666
            CUBE777 -> R.drawable._777
            ONE_HANDED ->  R.drawable._333oh
            PYRA ->  R.drawable.pyram
            SKEWB -> R.drawable.skewb
            SQ1 -> R.drawable.sq1
            CLOCK -> R.drawable.clock
            MEGA -> R.drawable.minx
            BF333 -> R.drawable._333bf
            BF444 ->  R.drawable._444bf
            BF555 ->  R.drawable._555bf
            MBLD -> R.drawable._333mbf
        }
    }


}