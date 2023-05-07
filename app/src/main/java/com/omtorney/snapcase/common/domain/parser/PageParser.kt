package com.omtorney.snapcase.common.domain.parser

import com.omtorney.snapcase.common.domain.court.Court
import com.omtorney.snapcase.common.domain.model.Case
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

interface PageParser {

    fun extractSchedule(document: Document, court: Court): List<Case>

    fun extractSearchResult(document: Document, court: Court): List<Case>

    suspend fun fillCase(case: Case, court: Court): Case

    suspend fun extractActText(url: String): String


    fun getPlaintiff(info: String): String

    fun getDefendant(info: String): String

    fun getCaseCategory(info: String): String

    fun getCaseNumber(numberString: String): String

    fun getCaseActUrl(element: Element, court: Court): String

    fun createPagesUrlsList(page: Document, searchUrl: String, court: Court): List<String>

    fun commonMethod() {
        println("hello")
    }
}