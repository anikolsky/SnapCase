package com.omtorney.snapcase.domain.parser

import com.omtorney.snapcase.domain.court.Court
import com.omtorney.snapcase.domain.repository.Repository
import javax.inject.Inject

class PageParserFactory @Inject constructor(
    private val repository: Repository
) {
    fun create(court: Court): PageParser {
        return when(court.type) {
            PageType.NoMsk -> PageParserNoMsk(repository)
            PageType.Msk -> PageParserMsk()
        }
    }
}
