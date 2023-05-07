package com.omtorney.snapcase.act.domain.usecase

import com.omtorney.snapcase.common.domain.court.Court
import com.omtorney.snapcase.common.domain.Repository
import com.omtorney.snapcase.common.domain.parser.PageParserFactory
import com.omtorney.snapcase.common.util.Resource
import com.omtorney.snapcase.common.util.handleException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoadActText @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(court: Court, url: String): Flow<Resource<String>> =
        flow {
            try {
                emit(Resource.Loading())
                val page = PageParserFactory(repository).create(court)
                val text = page.extractActText(url)
                emit(Resource.Success(text))
            } catch (e: Exception) {
                emit(Resource.Error(message = handleException(e)))
            }
        }
}