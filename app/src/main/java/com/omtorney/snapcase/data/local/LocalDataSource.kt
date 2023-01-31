package com.omtorney.snapcase.data.local

import com.omtorney.snapcase.domain.model.Case
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getFavoriteCases(): Flow<List<Case>>

    suspend fun deleteFavorite(case: Case)

    suspend fun addFavorite(case: Case)

    suspend fun updateFavorite(case: Case)

    suspend fun checkCase(uid: String): Int
}