package com.omtorney.snapcase.common.domain.usecase

import com.omtorney.snapcase.act.domain.usecase.LoadActText
import com.omtorney.snapcase.detail.domain.usecase.GetCaseByNumber
import com.omtorney.snapcase.favorites.domain.usecase.GetFavoriteCases
import com.omtorney.snapcase.recent.domain.usecase.ClearRecentCases
import com.omtorney.snapcase.recent.domain.usecase.GetRecentCases
import com.omtorney.snapcase.schedule.domain.usecase.ShowSchedule
import com.omtorney.snapcase.search.domain.usecase.SearchCase

data class CaseUseCases(
    val checkCase: CheckCase,
    val clearRecentCases: ClearRecentCases,
    val deleteCase: DeleteCase,
    val fillCase: FillCase,
    val getCaseByNumber: GetCaseByNumber,
    val getFavoriteCases: GetFavoriteCases,
    val getRecentCases: GetRecentCases,
    val loadActText: LoadActText,
    val saveCase: SaveCase,
    val searchCase: SearchCase,
    val showSchedule: ShowSchedule
)
