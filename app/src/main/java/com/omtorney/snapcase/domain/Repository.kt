package com.omtorney.snapcase.domain

import com.omtorney.snapcase.data.model.Case

interface Repository {

    suspend fun getHtmlData(url: String): String
    suspend fun deleteFavorite(case: Case)
    suspend fun addFavorite(case: Case)
    suspend fun updateFavorite(case: Case)
    suspend fun checkCase(uid: String): Int
}