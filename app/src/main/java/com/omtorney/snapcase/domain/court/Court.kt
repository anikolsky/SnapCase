package com.omtorney.snapcase.domain.court

import com.omtorney.snapcase.domain.parser.PageType

interface Court {
    val title: String
    val type: PageType
    val baseUrl: String
    fun getScheduleQuery(date: String): String {
        return when (this.type) {
            PageType.NoMsk -> getScheduleQueryNoMsk(this.baseUrl, date)
            PageType.Msk -> TODO()
        }
    }
    fun getSearchQuery(caseType: CaseType, sideName: String, caseNumber: String): String {
        return when (this.type) {
            PageType.NoMsk -> getSearchQueryNoMsk(this.baseUrl, caseType, sideName, caseNumber)
            PageType.Msk -> TODO()
        }
    }
}