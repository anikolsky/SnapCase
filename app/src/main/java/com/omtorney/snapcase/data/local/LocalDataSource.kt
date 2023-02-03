package com.omtorney.snapcase.data.local

import com.omtorney.snapcase.domain.model.Case
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun saveCase(case: Case)

    suspend fun deleteFavorite(case: Case)

    suspend fun updateFavorite(case: Case)

    suspend fun checkCase(uid: String): Int

    suspend fun clearRecentCases()

    suspend fun getCaseByNumber(number: String): Case?

    fun getFavoriteCases(): Flow<List<Case>>

    fun getRecentCases(): Flow<List<Case>>

    val getAccentColor: Flow<Long>

    suspend fun setAccentColor(color: Long)

    val getInitialColor: Long
}