package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.domain.court.Court
import com.omtorney.snapcase.domain.Repository
import com.omtorney.snapcase.domain.parser.PageParserFactory
import com.omtorney.snapcase.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoadActText @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(court: Court, url: String): Flow<Resource<ArrayList<String>>> =
        flow {
            try {
                emit(Resource.Loading())
                val page = PageParserFactory(repository).create(court)
                val text = page.extractActText(url)
                emit(Resource.Success(text))
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: "Unexpected error while fetching act text"))
            }
        }
}