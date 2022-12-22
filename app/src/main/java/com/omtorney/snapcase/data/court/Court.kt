package com.omtorney.snapcase.data.court

import com.omtorney.snapcase.domain.parser.PageType

interface Court {

    val type: PageType
    val basicUrl: String

    fun getScheduleQuery(date: String): String
    fun getGPKSearchQuery(sideName: String, caseNumber: String): String
    fun getKASSearchQuery(sideName: String, caseNumber: String): String
}