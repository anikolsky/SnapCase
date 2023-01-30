package com.omtorney.snapcase.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omtorney.snapcase.domain.model.Case

@Database(entities = [Case::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun caseDao(): CaseDao
}