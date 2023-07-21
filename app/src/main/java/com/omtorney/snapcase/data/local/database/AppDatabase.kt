package com.omtorney.snapcase.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.model.CaseConverters

@Database(entities = [Case::class], version = 1, exportSchema = false)
@TypeConverters(CaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun caseDao(): CaseDao
}
