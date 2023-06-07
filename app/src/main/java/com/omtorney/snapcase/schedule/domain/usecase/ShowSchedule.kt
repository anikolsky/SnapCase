package com.omtorney.snapcase.schedule.domain.usecase

import android.util.Log
import com.omtorney.snapcase.common.domain.Repository
import com.omtorney.snapcase.common.domain.court.Courts
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.parser.PageParserFactory
import com.omtorney.snapcase.common.util.Resource
import com.omtorney.snapcase.common.util.handleException
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
        } catch (e: Throwable) {
            emit(Resource.Error(message = handleException(e)))
            Log.d("TESTLOG", "[ShowSchedule] exception: ${e.localizedMessage}")
        }
    }
}