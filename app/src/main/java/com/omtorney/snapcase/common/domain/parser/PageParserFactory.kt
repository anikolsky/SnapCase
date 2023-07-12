package com.omtorney.snapcase.common.domain.parser

import com.omtorney.snapcase.common.domain.court.Court
import com.omtorney.snapcase.common.domain.Repository
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
