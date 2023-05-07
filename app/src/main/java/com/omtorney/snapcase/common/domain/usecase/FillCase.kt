package com.omtorney.snapcase.common.domain.usecase

import com.omtorney.snapcase.common.domain.Repository
import com.omtorney.snapcase.common.domain.court.Court
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.parser.PageParserFactory
import com.omtorney.snapcase.common.util.Resource
import com.omtorney.snapcase.common.util.handleException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FillCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(case: Case, court: Court): Flow<Resource<Case>> = flow {
        try {
            emit(Resource.Loading())
            val page = PageParserFactory(repository).create(court)
            val resultCase = page.fillCase(case, court)
            emit(Resource.Success(resultCase))
        } catch (e: Exception) {
            emit(Resource.Error(message = handleException(e)))
        }
    }
}