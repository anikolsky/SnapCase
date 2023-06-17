package com.omtorney.snapcase.common.domain.parser

import com.omtorney.snapcase.common.domain.court.Court
import com.omtorney.snapcase.common.domain.model.Case
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

interface PageParser {

    fun extractSchedule(document: Document, court: Court): List<Case>

    fun extractSearchResult(document: Document, court: Court): List<Case>

    suspend fun fetchCase(case: Case): Case

    suspend fun extractActText(url: String): String


    fun getAdministrativeInfo(info: String, case: Case): Case

    fun getCivilInfo(info: String, case: Case): Case

    fun getOtherInfo(info: String, case: Case): Case

    fun getCategory(info: String): String

    fun getCaseNumber(numberString: String): String

    fun getCaseActUrl(element: Element, court: Court): String

    fun createPagesUrlsList(page: Document, searchUrl: String, court: Court): List<String>

    fun commonMethod() {
        println("hello")
    }
}