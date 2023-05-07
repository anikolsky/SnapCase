package com.omtorney.snapcase.common.domain.parser

import com.omtorney.snapcase.common.domain.court.Court
import com.omtorney.snapcase.common.domain.model.Case
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class PageParserMsk : PageParser {

    override fun extractSchedule(document: Document, court: Court): List<Case> {
        TODO("Not yet implemented")
    }

    override fun extractSearchResult(document: Document, court: Court): List<Case> {
        TODO("Not yet implemented")
    }

    override suspend fun fillCase(case: Case, court: Court): Case {
        TODO("Not yet implemented")
    }

    override suspend fun extractActText(url: String): String {
        TODO("Not yet implemented")
    }

    override fun getPlaintiff(info: String): String {
        TODO("Not yet implemented")
    }

    override fun getDefendant(info: String): String {
        TODO("Not yet implemented")
    }

    override fun getCaseCategory(info: String): String {
        TODO("Not yet implemented")
    }

    override fun getCaseNumber(numberString: String): String {
        TODO("Not yet implemented")
    }

    override fun getCaseActUrl(element: Element, court: Court): String {
        TODO("Not yet implemented")
    }

    override fun createPagesUrlsList(page: Document, searchUrl: String, court: Court): List<String> {
        TODO("Not yet implemented")
    }
}