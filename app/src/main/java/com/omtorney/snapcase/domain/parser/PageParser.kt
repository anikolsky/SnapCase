package com.omtorney.snapcase.domain.parser

import com.omtorney.snapcase.data.court.Court
import com.omtorney.snapcase.data.model.Case
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

interface PageParser {

    fun extractSchedule(html: String, court: Court): List<Case>
    fun extractSearchResult(html: String, court: Court): List<Case>
    suspend fun fillCase(case: Case, court: Court): Case
    suspend fun extractActText(url: String): ArrayList<String>

    fun getCasePlaintiff(info: String): String
    fun getCaseDefendant(info: String): String
    fun getCaseCategory(info: String): String
    fun getCaseNumber(numberString: String): String
    fun getCaseActUrl(element: Element, court: Court): String
    fun createPagesUrlsList(page: Document, searchUrl: String, court: Court): List<String>

    fun commonMethod() {
        println("hello")
    }
}