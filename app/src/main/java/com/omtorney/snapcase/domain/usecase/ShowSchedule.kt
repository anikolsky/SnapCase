package com.omtorney.snapcase.domain.usecase

import android.util.Log
import com.omtorney.snapcase.domain.Repository
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.parser.PageParserFactory
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
            val court = Courts.getCourtList().find { it.title == courtTitle } ?: Courts.Dmitrov
            val html = repository.getJsoupDocument(court.getScheduleQuery(date)).toString()
            val page = PageParserFactory(repository).create(court)
            val cases = page.extractSchedule(html, court)
            emit(Resource.Success(cases))
        } catch (e: Exception) {
            emit(Resource.Error(message = handleException(e)))
            Log.d("TESTLOG", "ShowSchedule: error: ${e.localizedMessage}")
        }
    }
}