package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.domain.court.Court
import com.omtorney.snapcase.data.remote.RemoteDataSource
import com.omtorney.snapcase.domain.Repository
import com.omtorney.snapcase.domain.parser.PageParserFactory
import javax.inject.Inject

class LoadActText @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(court: Court, url: String): ArrayList<String> {
        val page = PageParserFactory(repository).create(court)
        return page.extractActText(url)
    }
}