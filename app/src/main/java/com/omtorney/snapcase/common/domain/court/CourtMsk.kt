package com.omtorney.snapcase.common.domain.court

import com.omtorney.snapcase.common.domain.model.CaseType
import com.omtorney.snapcase.common.domain.parser.PageType

abstract class CourtMsk(
    override val title: String,
    override val baseUrl: String
) : Court(title, PageType.Msk, baseUrl) {

    override fun getScheduleQuery(date: String): String {
        return if (date.isEmpty()) "$baseUrl/"
        else "$baseUrl/$date"
    }

    override fun getSearchQuery(caseType: CaseType, sideName: String, caseNumber: String): String {
        return when (caseType) {
            CaseType.GPK -> TODO()
            CaseType.KAS -> TODO()
        }
    }
}
