package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.data.court.Court
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.data.remote.RemoteDataSource
import com.omtorney.snapcase.domain.parser.PageParserFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FillCaseUseCase @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun execute(case: Case, court: Court): Case {
        val page = PageParserFactory(remoteDataSource).createPageParser(court)
        val caseFull = withContext(Dispatchers.IO) {
            page.fillCase(case, court)
        }
        return caseFull
    }
}