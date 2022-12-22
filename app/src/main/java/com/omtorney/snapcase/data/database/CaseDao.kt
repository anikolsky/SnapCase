package com.omtorney.snapcase.data.database

import androidx.room.*
import com.omtorney.snapcase.data.model.Case
import kotlinx.coroutines.flow.Flow

@Dao
interface CaseDao {

    @Query("SELECT * FROM cases")
    fun getAll(): Flow<List<Case>>

    @Query("SELECT COUNT() FROM cases WHERE uid =:uid")
    suspend fun check(uid: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(case: Case)

    @Delete
    suspend fun delete(case: Case)

    @Update
    suspend fun update(case: Case)
}