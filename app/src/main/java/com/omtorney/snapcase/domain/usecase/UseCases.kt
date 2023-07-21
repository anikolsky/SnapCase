package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.domain.usecase.act.LoadActText
import com.omtorney.snapcase.domain.usecase.common.CheckCase
import com.omtorney.snapcase.domain.usecase.common.DeleteCase
import com.omtorney.snapcase.domain.usecase.common.FetchCase
import com.omtorney.snapcase.domain.usecase.common.SaveCase
import com.omtorney.snapcase.domain.usecase.detail.GetCaseByNumber
import com.omtorney.snapcase.domain.usecase.favorites.GetFavoriteCases
import com.omtorney.snapcase.domain.usecase.firebase.CreateBackup
import com.omtorney.snapcase.domain.usecase.firebase.DeleteBackup
import com.omtorney.snapcase.domain.usecase.firebase.GetUser
import com.omtorney.snapcase.domain.usecase.firebase.UpdateBackup
import com.omtorney.snapcase.domain.usecase.recent.ClearRecentCases
import com.omtorney.snapcase.domain.usecase.recent.GetRecentCases
import com.omtorney.snapcase.domain.usecase.schedule.ShowSchedule
import com.omtorney.snapcase.domain.usecase.search.SearchCase
import com.omtorney.snapcase.domain.usecase.settings.EnableDarkTheme
import com.omtorney.snapcase.domain.usecase.settings.GetAccentColor
import com.omtorney.snapcase.domain.usecase.settings.GetCaseCheckPeriod
import com.omtorney.snapcase.domain.usecase.settings.GetSelectedCourt
import com.omtorney.snapcase.domain.usecase.settings.IsDarkThemeEnabled
import com.omtorney.snapcase.domain.usecase.settings.SetAccentColor
import com.omtorney.snapcase.domain.usecase.settings.SetCaseCheckPeriod
import com.omtorney.snapcase.domain.usecase.settings.SetSelectedCourt

data class UseCases(
    val checkCase: CheckCase,
    val clearRecentCases: ClearRecentCases,
    val deleteCase: DeleteCase,
    val fetchCase: FetchCase,
    val getCaseByNumber: GetCaseByNumber,
    val getFavoriteCases: GetFavoriteCases,
    val getRecentCases: GetRecentCases,
    val loadActText: LoadActText,
    val saveCase: SaveCase,
    val searchCase: SearchCase,
    val showSchedule: ShowSchedule,
    // settings
    val enableDarkTheme: EnableDarkTheme,
    val isDarkThemeEnabled: IsDarkThemeEnabled,
    val getAccentColor: GetAccentColor,
    val setAccentColor: SetAccentColor,
    val getCaseCheckPeriod: GetCaseCheckPeriod,
    val setCaseCheckPeriod: SetCaseCheckPeriod,
    val getSelectedCourt: GetSelectedCourt,
    val setSelectedCourt: SetSelectedCourt,
    // firebase
    val getUser: GetUser,
    val createBackup: CreateBackup,
    val deleteBackup: DeleteBackup,
    val updateBackup: UpdateBackup
)
