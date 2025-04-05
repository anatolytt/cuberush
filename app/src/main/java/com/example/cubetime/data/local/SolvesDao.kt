package com.example.cubetime.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cubetime.data.model.Penalties
import com.example.cubetime.data.model.Session
import com.example.cubetime.data.model.ShortSolve
import com.example.cubetime.data.model.Solve
import kotlinx.coroutines.flow.Flow

@Dao
interface SolvesDao {
    // Solves
    @Query("SELECT * FROM Solve WHERE sessionName = :sessionName")
    fun getAllSessionSolves(sessionName: String) : Flow<List<Solve>>

    @Query("SELECT id, result, penalties, date " +
            "FROM Solve WHERE sessionName = :sessionName " +
            "ORDER BY id DESC")
    fun getAllShortSessionSolves(sessionName: String) : Flow<List<ShortSolve>>

    @Query("SELECT * FROM Solve WHERE id = :id")
    fun getSolveById(id: Int) : Solve

    @Query("UPDATE Solve SET penalties = :newValue WHERE id = :id")
    fun updatePenalty(id: Int, newValue: Penalties)

    @Query("UPDATE Solve SET comment = :newValue WHERE id = :id")
    fun updateComment(id: Int, newValue: String) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSolve(solve: Solve) : Unit

    @Query("SELECT id FROM Solve ORDER BY id DESC")
    fun getLastSolveId() : Int



    @Query("DELETE FROM Solve WHERE id = :id")
    fun deleteById(id: Int) : Int


    // Sessions
    @Query("SELECT * FROM Session")
    fun getAllSessions() : Flow<List<Session>>

    @Query("SELECT * FROM Session WHERE name = :name")
    fun getSessionByName(name: String) : Session

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSession(session: Session) : Unit

}