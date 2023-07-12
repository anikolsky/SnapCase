package com.omtorney.snapcase.common.data.database

import androidx.room.*
import com.omtorney.snapcase.common.domain.model.Case
import kotlinx.coroutines.flow.Flow

@Dao
interface CaseDao {

    @Query("SELECT * FROM cases")
    fun getFavoriteCases(): Flow<List<Case>>

    @Query("SELECT * FROM cases WHERE number = :number")
    suspend fun getCaseByNumber(number: String): Case?

    @Query("SELECT COUNT() FROM cases WHERE uid = :uid")
    suspend fun checkCase(uid: String): Int

    @Upsert
    suspend fun upsert(case: Case)

    @Delete
    suspend fun delete(case: Case)
}
