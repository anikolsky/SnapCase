package com.omtorney.snapcase.domain

import com.omtorney.snapcase.domain.model.Case
import kotlinx.coroutines.flow.Flow
import org.jsoup.nodes.Document

interface Repository {

    suspend fun getHtmlData(url: String): String

    suspend fun getJsoupDocument(url: String): Document?

    fun getFavoriteCases(): Flow<List<Case>>

    suspend fun deleteFavorite(case: Case)

    suspend fun addFavorite(case: Case)

    suspend fun updateFavorite(case: Case)

    suspend fun checkCase(uid: String): Int
}