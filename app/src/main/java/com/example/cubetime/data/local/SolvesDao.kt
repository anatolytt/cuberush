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
    @Query("SELECT * FROM Solve WHERE sessionId = :sessionId")
    fun getAllSessionSolves(sessionId: Int) : Flow<List<Solve>>

//    @Query("SELECT id, result, penalties, date " +
//            "FROM Solve WHERE sessionId = :sessionId " +
//            "ORDER BY id DESC")
//    fun getAllShortSessionSolves(sessionId: Int) : Flow<List<ShortSolve>>
    //Поиск
    @Query("SELECT id, result, penalties, date " +
            "FROM Solve " +
            "WHERE sessionId = :sessionId AND result>= :min AND result<:max " +
            "ORDER BY id DESC")
    fun getAllShortSessionSolves(min:Int,max:Int,sessionId: Int) : Flow<List<ShortSolve>>


    //Отсортировынный по убыванию список сборок
    @Query(" SELECT id,result,penalties,date" + " FROM Solve WHERE sessionId = :sessionId"+
            " ORDER BY result DESC")
    fun getSortedShortSolveEnd(sessionId: Int): Flow<List<ShortSolve>>
    //Отсортировынный по возрастанию список сборок
    @Query(" SELECT id,result,penalties,date" + " FROM Solve WHERE sessionId = :sessionId"+
            " ORDER BY result")
    fun getSortedShortSolveStart(sessionId: Int): Flow<List<ShortSolve>>



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

    @Query("SELECT * FROM Session WHERE id = :id")
    fun getSessionById(id: Int) : Session

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSession(session: Session) : Long

    @Query("DELETE FROM Session WHERE id = :id")
    fun deleteSessionById(id: Int) : Int

    @Query("DELETE FROM Solve WHERE sessionId = :id")
    fun deleteSessionSolves(id: Int)

    @Query("UPDATE Session SET name = :newName WHERE id = :id")
    fun updateSessionName(id: Int, newName:String)

    @Query("SELECT id FROM session ORDER BY id DESC LIMIT 1")
    fun getLastAddedSessionId(): Int

    @Query("SELECT COUNT(*) FROM session")
    fun getSessionCount(): Int

    @Query("SELECT id FROM session WHERE name = :name")
    fun getSessionId(name: String): Int



}