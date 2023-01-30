package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.domain.court.Court
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.data.remote.RemoteDataSource
import com.omtorney.snapcase.domain.Repository
import com.omtorney.snapcase.domain.parser.PageParserFactory
import javax.inject.Inject

class FillCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(case: Case, court: Court): Case {
        val page = PageParserFactory(repository).create(court)
        return page.fillCase(case, court)
    }
}