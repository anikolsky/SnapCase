package com.omtorney.snapcase.common.data.database

import androidx.room.*
import com.omtorney.snapcase.common.domain.model.Case
import kotlinx.coroutines.flow.Flow

@Dao
interface CaseDao {

    @Query("SELECT * FROM cases WHERE is_favorite = 1")
    fun getFavoriteCases(): Flow<List<Case>>

    @Query("SELECT * FROM cases WHERE is_favorite = 0")
    fun getRecentCases(): Flow<List<Case>>

    @Query("SELECT * FROM cases WHERE number = :number")
    suspend fun getCaseByNumber(number: String): Case?

    @Query("SELECT COUNT() FROM cases WHERE uid =:uid")
    suspend fun checkCase(uid: String): Int

    @Query("DELETE FROM cases WHERE is_favorite = 0")
    suspend fun deleteRecentCases()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(case: Case)

    @Delete
    suspend fun delete(case: Case)
}
