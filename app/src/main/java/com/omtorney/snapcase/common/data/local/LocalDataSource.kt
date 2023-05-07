package com.omtorney.snapcase.common.data.local

import com.omtorney.snapcase.common.domain.model.Case
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
    val getSelectedCourt: Flow<String>
    suspend fun setSelectedCourt(courtTitle: String)
}