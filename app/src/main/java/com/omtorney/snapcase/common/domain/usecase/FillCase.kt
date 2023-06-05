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
            val cachedCase = repository.getCaseByNumber(case.number)
            if (cachedCase != null) {
                emit(Resource.Loading(data = cachedCase))
            } else {
                emit(Resource.Loading())
            }
            val page = PageParserFactory(repository).create(court)
            val fetchedCase = page.fillCase(case, court)
            repository.saveCase(case = fetchedCase)
            emit(Resource.Success(data = fetchedCase))
        } catch (e: Exception) {
            emit(Resource.Error(message = handleException(e)))
        }
    }
}
