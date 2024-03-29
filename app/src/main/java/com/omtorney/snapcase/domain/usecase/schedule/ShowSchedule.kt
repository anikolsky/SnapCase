package com.omtorney.snapcase.domain.usecase.schedule

import com.omtorney.snapcase.domain.repository.Repository
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.parser.PageParserFactory
import com.omtorney.snapcase.presentation.logd
import com.omtorney.snapcase.util.Resource
import com.omtorney.snapcase.util.handleException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShowSchedule @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(courtTitle: String, date: String): Flow<Resource<List<Case>>> = flow {
        try {
            emit(Resource.Loading())
            val court = Courts.getCourt(courtTitle)
            val document = repository.getJsoupDocument(court.getScheduleQuery(date))
            val page = PageParserFactory(repository).create(court)
            val cases = page.extractSchedule(document!!, court)
            emit(Resource.Success(data = cases))
        } catch (e: Exception) {
            emit(Resource.Error(message = handleException(e)))
            logd("ShowSchedule exception: ${e.localizedMessage}")
        }
    }
}