package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.domain.Repository
import com.omtorney.snapcase.domain.court.Court
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.parser.PageParserFactory
import com.omtorney.snapcase.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShowSchedule @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(court: Court, date: String): Flow<Resource<List<Case>>> = flow {
        try {
            emit(Resource.Loading())
            val html = repository.getHtmlData(court.getScheduleQuery(date))
            val page = PageParserFactory(repository).create(court)
            val cases = page.extractSchedule(html, court)
            emit(Resource.Success(cases))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "Unexpected error while fetching schedule"))
        }
    }
}