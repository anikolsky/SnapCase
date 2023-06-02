package com.omtorney.snapcase.common.data.database

import androidx.room.*
import com.omtorney.snapcase.common.domain.model.Case
import kotlinx.coroutines.flow.Flow

@Dao
interface CaseDao {

    @Query("SELECT * FROM cases WHERE is_favorite = 1")
    fun getFavorites(): Flow<List<Case>>

    @Query("SELECT * FROM cases WHERE is_favorite = 0")
    fun getRecent(): Flow<List<Case>>

    @Query("SELECT * FROM cases WHERE number = :number")
    suspend fun getCaseByNumber(number: String): Case?

    @Query("SELECT COUNT() FROM cases WHERE uid =:uid")
    suspend fun check(uid: String): Int

    @Query("DELETE FROM cases WHERE is_favorite = 0")
    suspend fun deleteRecent()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(case: Case)

    @Delete
    suspend fun delete(case: Case)

    @Update
    suspend fun update(case: Case)
}