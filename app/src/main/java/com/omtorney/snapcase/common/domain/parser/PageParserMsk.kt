package com.omtorney.snapcase.common.domain.parser

import com.omtorney.snapcase.common.domain.court.Court
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.model.ProcessStep
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class PageParserMsk : PageParser {

    override fun extractSchedule(document: Document, court: Court): List<Case> {
        TODO("Not yet implemented")
    }

    override fun extractSearchResult(document: Document, court: Court): List<Case> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchCase(case: Case): Case {
        TODO("Not yet implemented")
    }

    override fun fetchProcess(element: Element?): MutableList<ProcessStep> {
        TODO("Not yet implemented")
    }

    override fun fetchAppeal(element: Element?): MutableMap<String, String> {
        TODO("Not yet implemented")
    }

    override fun fetchParticipants(element: Element?): MutableList<String> {
        TODO("Not yet implemented")
    }

    override suspend fun extractActText(url: String): String {
        TODO("Not yet implemented")
    }

    override fun getAdministrativeInfo(info: String, case: Case): Case {
        TODO("Not yet implemented")
    }

    override fun getCivilInfo(info: String, case: Case): Case {
        TODO("Not yet implemented")
    }

    override fun getOtherInfo(info: String, case: Case): Case {
        TODO("Not yet implemented")
    }

    override fun getCategory(info: String): String {
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