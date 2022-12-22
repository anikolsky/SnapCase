package com.omtorney.snapcase.domain.parser

import com.omtorney.snapcase.data.court.Court
import com.omtorney.snapcase.data.model.Case
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class MskPageParser : PageParser {

    override fun extractSchedule(html: String, court: Court): List<Case> {
        TODO("Not yet implemented")
    }

    override fun extractSearchResult(html: String, court: Court): List<Case> {
        TODO("Not yet implemented")
    }

    override suspend fun fillCase(case: Case, court: Court): Case {
        TODO("Not yet implemented")
    }

    override suspend fun extractActText(url: String): ArrayList<String> {
        TODO("Not yet implemented")
    }

    override fun getCasePlaintiff(info: String): String {
        TODO("Not yet implemented")
    }

    override fun getCaseDefendant(info: String): String {
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