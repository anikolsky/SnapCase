package com.omtorney.snapcase.common.data.local

import com.omtorney.snapcase.common.domain.model.Case
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun saveCase(case: Case)
    suspend fun deleteFavorite(case: Case)
    suspend fun checkCase(uid: String): Int
    suspend fun getCaseByNumber(number: String): Case?
    fun getFavoriteCases(): Flow<List<Case>>
}
