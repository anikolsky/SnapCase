package com.omtorney.snapcase.domain.parser

import com.omtorney.snapcase.data.court.Court
import com.omtorney.snapcase.data.remote.RemoteDataSource
import javax.inject.Inject

class PageParserFactory @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    fun createPageParser(court: Court): PageParser {
        return when(court.type) {
            PageType.NoMsk -> NoMskPageParser(remoteDataSource)
            PageType.Msk -> MskPageParser()
        }
    }
}