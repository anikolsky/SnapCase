package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.data.court.Court
import com.omtorney.snapcase.data.remote.RemoteDataSource
import com.omtorney.snapcase.domain.parser.PageParserFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadActTextUseCase @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun execute(court: Court, url: String): ArrayList<String> {
        val page = PageParserFactory(remoteDataSource).createPageParser(court)
        val actText = withContext(Dispatchers.IO) {
            page.extractActText(url)
        }
        return actText
    }
}