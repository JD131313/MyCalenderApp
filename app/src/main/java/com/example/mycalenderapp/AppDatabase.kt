package com.example.mycalenderapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Memo::class, Checklist::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
    abstract fun checklistDao(): ChecklistDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {          // synchronized : cpu 관리에 도움
                val db = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "database-name"
                ).build()
                // INSTANCE = db
                db
            }
        }
    }
}