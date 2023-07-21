package com.omtorney.snapcase.di

import com.omtorney.snapcase.domain.usecase.act.LoadActText
import com.omtorney.snapcase.domain.repository.Repository
import com.omtorney.snapcase.domain.usecase.UseCases
import com.omtorney.snapcase.domain.usecase.common.CheckCase
import com.omtorney.snapcase.domain.usecase.common.DeleteCase
import com.omtorney.snapcase.domain.usecase.detail.GetCaseByNumber
import com.omtorney.snapcase.domain.usecase.common.SaveCase
import com.omtorney.snapcase.domain.usecase.common.FetchCase
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideCaseUseCases(repository: Repository): UseCases {
        return UseCases(
            checkCase = CheckCase(repository),
            clearRecentCases = ClearRecentCases(repository),
            deleteCase = DeleteCase(repository),
            fetchCase = FetchCase(repository),
            getCaseByNumber = GetCaseByNumber(repository),
            getFavoriteCases = GetFavoriteCases(repository),
            getRecentCases = GetRecentCases(repository),
            loadActText = LoadActText(repository),
            saveCase = SaveCase(repository),
            searchCase = SearchCase(repository),
            showSchedule = ShowSchedule(repository),
            // settings
            enableDarkTheme = EnableDarkTheme(repository),
            isDarkThemeEnabled = IsDarkThemeEnabled(repository),
            getAccentColor = GetAccentColor(repository),
            setAccentColor = SetAccentColor(repository),
            getCaseCheckPeriod = GetCaseCheckPeriod(repository),
            setCaseCheckPeriod = SetCaseCheckPeriod(repository),
            getSelectedCourt = GetSelectedCourt(repository),
            setSelectedCourt = SetSelectedCourt(repository),
            // firebase
            getUser = GetUser(repository),
            createBackup = CreateBackup(repository),
            updateBackup = UpdateBackup(repository),
            deleteBackup = DeleteBackup(repository)
        )
    }
}
