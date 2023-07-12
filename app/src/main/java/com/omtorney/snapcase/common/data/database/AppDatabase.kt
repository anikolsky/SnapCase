package com.omtorney.snapcase.common.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.model.CaseConverters

@Database(entities = [Case::class], version = 1, exportSchema = false)
@TypeConverters(CaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun caseDao(): CaseDao
}
