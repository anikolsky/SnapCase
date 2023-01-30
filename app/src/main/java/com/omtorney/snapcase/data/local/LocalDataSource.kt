package com.omtorney.snapcase.data.local

import com.omtorney.snapcase.domain.model.Case

interface LocalDataSource {

    suspend fun deleteFavorite(case: Case)

    suspend fun addFavorite(case: Case)

    suspend fun updateFavorite(case: Case)

    suspend fun checkCase(uid: String): Int
}