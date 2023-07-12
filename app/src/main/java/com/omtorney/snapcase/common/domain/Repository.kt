package com.omtorney.snapcase.common.domain

import com.omtorney.snapcase.common.domain.model.Case
import kotlinx.coroutines.flow.Flow
import org.jsoup.nodes.Document

interface Repository {

    suspend fun getJsoupDocument(url: String): Document?

    suspend fun saveCase(case: Case)
    suspend fun deleteFavorite(case: Case)
    suspend fun checkCase(uid: String): Int
    suspend fun getCaseByNumber(number: String): Case?
    fun getFavoriteCases(): Flow<List<Case>>
}
