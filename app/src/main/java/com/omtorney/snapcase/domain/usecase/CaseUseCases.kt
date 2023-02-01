package com.omtorney.snapcase.domain.usecase

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
    val showSchedule: ShowSchedule,
    val updateCase: UpdateCase
)
