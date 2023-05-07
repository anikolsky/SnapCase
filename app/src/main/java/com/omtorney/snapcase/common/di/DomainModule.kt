package com.omtorney.snapcase.common.di

import com.omtorney.snapcase.act.domain.usecase.LoadActText
import com.omtorney.snapcase.common.domain.Repository
import com.omtorney.snapcase.common.domain.usecase.CaseUseCases
import com.omtorney.snapcase.common.domain.usecase.CheckCase
import com.omtorney.snapcase.common.domain.usecase.DeleteCase
import com.omtorney.snapcase.detail.domain.usecase.GetCaseByNumber
import com.omtorney.snapcase.common.domain.usecase.SaveCase
import com.omtorney.snapcase.common.domain.usecase.FillCase
import com.omtorney.snapcase.favorites.domain.usecase.GetFavoriteCases
import com.omtorney.snapcase.favorites.domain.usecase.UpdateCase
import com.omtorney.snapcase.recent.domain.usecase.ClearRecentCases
import com.omtorney.snapcase.recent.domain.usecase.GetRecentCases
import com.omtorney.snapcase.schedule.domain.usecase.ShowSchedule
import com.omtorney.snapcase.search.domain.usecase.SearchCase
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
}