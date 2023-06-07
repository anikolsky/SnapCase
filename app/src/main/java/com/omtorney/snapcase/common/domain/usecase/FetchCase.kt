package com.omtorney.snapcase.common.domain.usecase

import com.omtorney.snapcase.common.domain.Repository
import com.omtorney.snapcase.common.domain.court.Court
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.parser.PageParserFactory
import com.omtorney.snapcase.common.presentation.logd
import com.omtorney.snapcase.common.util.Resource
import com.omtorney.snapcase.common.util.handleException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(case: Case, court: Court): Flow<Resource<Case>> = flow {
        try {
            val savedCase = repository.getCaseByNumber(case.number)
            if (savedCase != null) {
                emit(Resource.Loading(data = savedCase))
            } else {
                emit(Resource.Loading())
            }
            val page = PageParserFactory(repository).create(court)
            val fetchedCase = page.fetchCase(case)
//            repository.saveCase(case = fetchedCase)
            emit(Resource.Success(data = fetchedCase))
        } catch (e: Exception) {
            logd("Error while fetching case: ${e.localizedMessage ?: "Unexpected error"}")
            emit(Resource.Error(message = handleException(e)))
        }
    }
}
