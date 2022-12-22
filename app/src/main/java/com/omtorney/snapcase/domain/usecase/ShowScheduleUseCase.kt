package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.data.court.Court
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.data.remote.RemoteDataSource
import com.omtorney.snapcase.domain.parser.PageParserFactory
import com.omtorney.snapcase.domain.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShowScheduleUseCase @Inject constructor(
    private val repository: Repository,
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun execute(court: Court, date: String): List<Case> {
        val html = withContext(Dispatchers.IO) {
            repository.getHtmlData(court.getScheduleQuery(date))
        }
        val page = PageParserFactory(remoteDataSource).createPageParser(court)
        return page.extractSchedule(html, court)
    }
}