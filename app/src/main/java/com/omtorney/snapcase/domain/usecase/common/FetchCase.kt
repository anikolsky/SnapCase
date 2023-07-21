package com.omtorney.snapcase.domain.usecase.common

import com.omtorney.snapcase.domain.repository.Repository
import com.omtorney.snapcase.domain.court.Court
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.parser.PageParserFactory
import com.omtorney.snapcase.presentation.logd
import com.omtorney.snapcase.util.Resource
import com.omtorney.snapcase.util.handleException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(
        case: Case,
        court: Court,
        isFavorite: Boolean
    ): Flow<Resource<Case>> = flow {
        try {
            val savedCase = repository.getCaseByNumber(case.number)
            if (savedCase != null) {
                emit(Resource.Loading(data = savedCase))
            } else {
                emit(Resource.Loading())
            }
            val parser = PageParserFactory(repository).create(court)
            val fetchedCase = parser.fetchCase(case, isFavorite)
//            repository.saveCase(case = fetchedCase)
            emit(Resource.Success(data = fetchedCase))
        } catch (e: Exception) {
            logd("Error while fetching case: ${e.localizedMessage ?: "Unexpected error"}")
            emit(Resource.Error(message = handleException(e)))
        }
    }
}
