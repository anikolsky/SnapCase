package com.omtorney.snapcase.di

import com.omtorney.snapcase.domain.Repository
import com.omtorney.snapcase.domain.usecase.*
import com.omtorney.snapcase.domain.usecase.settings.*
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
    fun provideCaseUseCases(repository: Repository): CaseUseCases {
        return CaseUseCases(
            checkCase = CheckCase(repository),
            clearRecentCases = ClearRecentCases(repository),
            deleteCase = DeleteCase(repository),
            fillCase = FillCase(repository),
            getCaseByNumber = GetCaseByNumber(repository),
            getFavoriteCases = GetFavoriteCases(repository),
            getRecentCases = GetRecentCases(repository),
            loadActText = LoadActText(repository),
            saveCase = SaveCase(repository),
            searchCase = SearchCase(repository),
            showSchedule = ShowSchedule(repository),
            updateCase = UpdateCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideSettingsUseCases(repository: Repository): SettingsUseCases {
        return SettingsUseCases(
            getAccentColor = GetAccentColor(repository),
            setAccentColor = SetAccentColor(repository),
            getSelectedCourt = GetSelectedCourt(repository),
            setSelectedCourt = SetSelectedCourt(repository)
        )
    }
}