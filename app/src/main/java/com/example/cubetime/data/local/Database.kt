package com.example.cubetime.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cubetime.data.model.entities.Session
import com.example.cubetime.data.model.entities.Solve
import com.example.cubetime.data.model.entities.solvesAverages
import com.example.cubetime.utils.Converters

@Database(entities = [Solve::class, Session::class, solvesAverages::class], version = 25)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun SolvesDao() : SolvesDao

    // реализуем синглтон
    companion object {
        private var INSTANCE: AppDatabase? = null // хранит единственный экземпляр БД
        fun init(context: Context) : AppDatabase {
            var instance = INSTANCE
            synchronized(this) {    // чтобы два потока случайно не создали две БД
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "solvesdb"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance!!
            }
        }

        fun getInstance() : AppDatabase {
            return INSTANCE!!
        }
    }
}