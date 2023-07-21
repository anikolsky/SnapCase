package com.omtorney.snapcase.domain.court

import com.omtorney.snapcase.domain.model.CaseType
import com.omtorney.snapcase.domain.parser.PageType

abstract class Court(
    open val title: String,
    open val type: PageType,
    open val baseUrl: String
) {
    abstract fun getScheduleQuery(date: String): String
    abstract fun getSearchQuery(caseType: CaseType, sideName: String, caseNumber: String): String
}
