package com.example.cubetime.data.local

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.cubetime.data.model.AverageResult
import com.example.cubetime.data.model.Stat
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.Session
import com.example.cubetime.data.model.ShortSolve
import com.example.cubetime.data.model.Solve
import com.example.cubetime.data.model.StatType
import com.example.cubetime.data.model.solvesAverages
import kotlinx.coroutines.flow.Flow

@Dao
interface SolvesDao {
    // Solves
    @Query("SELECT * FROM Solve WHERE sessionId = :sessionId")
    fun getAllSessionSolves(sessionId: Int): Flow<List<Solve>>


    @Query(
        "SELECT id, result, penalties, date " +
                "FROM Solve WHERE sessionId = :sessionId " +
                "ORDER BY id DESC"
    )
    fun getAllShortSessionSolves(sessionId: Int): Flow<List<ShortSolve>>


    @Query("SELECT * FROM Solve WHERE id = :id")
    fun getSolveById(id: Int): Solve


    @Query("UPDATE Solve SET penalties = :newValue WHERE id = :id")
    fun updatePenalty(id: Int, newValue: Penalties)


    @Query("UPDATE Solve SET comment = :newValue WHERE id = :id")
    fun updateComment(id: Int, newValue: String): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSolve(solve: Solve): Unit


    @Query("SELECT id FROM Solve ORDER BY id DESC")
    fun getLastSolveId(): Int


    @Query("DELETE FROM Solve WHERE id = :id")
    fun deleteByIdFromSolves(id: Int): Int

    @Query("DELETE FROM solvesAverages WHERE solveId = :id")
    fun deleteByIdFromAverages(id: Int): Int


    @Transaction
    fun deleteSolvesById(ids: List<Int>) {
        ids.forEach { id ->
            deleteByIdFromSolves(id)
            deleteByIdFromAverages(id)
            Log.d("SOLVE DELETED", id.toString())
        }

    }


    @Query("SELECT * FROM Session")
    fun getAllSessions(): Flow<List<Session>>


    @Query("SELECT * FROM Session WHERE id = :id")
    fun getSessionById(id: Int): Session


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSession(session: Session): Long


    @Query("DELETE FROM Session WHERE id = :id")
    fun deleteSessionById(id: Int): Int


    @Query("DELETE FROM Solve WHERE sessionId = :id")
    fun deleteSessionSolves(id: Int)


    @Query("UPDATE Session SET name = :newName WHERE id = :id")
    fun updateSessionName(id: Int, newName: String)


    @Query("SELECT id FROM session ORDER BY id DESC LIMIT 1")
    fun getLastAddedSessionId(): Int


    @Query("SELECT COUNT(*) FROM session")
    fun getSessionCount(): Int


    @Query("SELECT id FROM session WHERE name = :name")
    fun getSessionId(name: String): Int


    @Query("SELECT COUNT(*) FROM Solve WHERE sessionId = :sessionId")
    fun countSolves(sessionId: Int): Flow<Int>


    @Query("SELECT * FROM Stat WHERE (sessionId = :sessionId) ")
    fun getAllStats(sessionId: Int): Flow<List<Stat>>


    @Upsert()
    fun updateSolveAverage(solveAverage: solvesAverages)

    @Transaction
    fun updateSolvesAverages(solvesAverages: List<solvesAverages>) {
        solvesAverages.forEach {
            updateSolveAverage(it)
        }
    }


    @Query(
        """
        SELECT result, solveId
        FROM solvesAverages 
        WHERE 
            sessionId = :sessionId 
            AND avgType = :avgType
        ORDER BY 
            CASE
                WHEN result = -1 then 1000000   -- NULL при сортировке всегда в конце
                WHEN result = -3 then 1000001
                ELSE result
            END,
        result 
        LIMIT 1
        """
    )
    fun getSessionBestAverage(sessionId: Int, avgType: StatType): AverageResult


    @Query(
        """
        SELECT result, solveId 
        FROM solvesAverages 
        WHERE 
            sessionId = :sessionId 
            AND avgType = :avgType
        ORDER BY solveId DESC 
        LIMIT 1
        """
    )
    fun getSessionCurrentAverage(sessionId: Int, avgType: StatType): AverageResult


    @Query(
        """
        SELECT result, id AS solveId
        FROM Solve
        WHERE sessionId = :sessionId
        ORDER BY 
            CASE
                WHEN penalties = -1 then 1000000
                WHEN penalties = 1 then result + 2000
                ELSE result
            END,
        result
        LIMIT 1
    """
    )
    fun getBestSingle(sessionId: Int): AverageResult


    @Query("""
        SELECT * 
        FROM Solve
        WHERE sessionId = :sessionId AND id <= :lastSolveId
        ORDER BY id DESC
        LIMIT CASE WHEN :solvesInAvg = 0 THEN -1 ELSE :solvesInAvg END
    """)
    suspend fun getAverageSolves(sessionId: Int, solvesInAvg: Int, lastSolveId: Int): List<Solve>?




    fun getMean(): Int {
        return -3
    }


    @Transaction
    fun getAllSessionBestAverages(sessionId: Int): Map<StatType, AverageResult> {
        val result = mutableMapOf<StatType, AverageResult>()
        StatType.entries.forEach { statType ->
            if (statType == StatType.SINGLE) {
                result[statType] = getBestSingle(sessionId)
            } else if (statType != StatType.MEAN) {
                result[statType] = getSessionBestAverage(sessionId, statType)
            }
        }
        return result
    }


    @Transaction
    fun getAllSessionCurrentAverages(sessionId: Int): Map<StatType, AverageResult> {
        val result = mutableMapOf<StatType, AverageResult>()
        StatType.entries.forEach { statType ->
            if (statType != StatType.SINGLE) {
                result[statType] = getSessionCurrentAverage(sessionId, statType)
            }
        }
        return result
    }
}



//    @Query("UPDATE Stat SET result = :result WHERE (sessionId=:sessionId) AND (isBestStat = false) AND (statType = :statType)")
//    fun updateStat(statType: Int, sessionId: Int, result: Int)
//
//    @Query("UPDATE Stat SET result = :result WHERE (sessionId=:sessionId) AND (:result < result OR result = -3) AND (isBestStat = true) AND (statType = :statType)")
//    fun updateBestStat(statType: Int, sessionId: Int, result: Int)
//
//    @Query("UPDATE Stat SET result = :result WHERE (sessionId=:sessionId) AND (isBestStat = true) AND (statType = :statType)")
//    fun strictUpdateBestStat(statType: Int, sessionId: Int, result: Int)
//

//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insertSessionStatsOrIgnore(newStats: List<Stat>)
//
//    @Transaction
//    fun updateCurrentStats(newStats: List<Stat>, sessionId: Int){
//        insertSessionStatsOrIgnore(newStats)
//        newStats.forEach { stat ->
//            updateStat(stat.statType, stat.sessionId, stat.result)
//        }
//    }
//
//    @Transaction
//    fun updateBestStats(newStats: List<Stat>, sessionId: Int){
//        insertSessionStatsOrIgnore(newStats)
//        newStats.forEach { stat ->
//            updateBestStat(stat.statType, stat.sessionId, stat.result)
//        }
//    }
//
//    @Transaction
//    fun strictUpdateBest(newStats: List<Stat>, sessionId: Int){
//        insertSessionStatsOrIgnore(newStats)
//        newStats.forEach { stat ->
//            strictUpdateBestStat(stat.statType, stat.sessionId, stat.result)
//        }
//    }
//




