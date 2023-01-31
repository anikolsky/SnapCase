package com.omtorney.snapcase.di

import com.omtorney.snapcase.domain.Repository
import com.omtorney.snapcase.domain.usecase.*
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
            deleteCase = DeleteCase(repository),
            fillCase = FillCase(repository),
            getFavoriteCases = GetFavoriteCases(repository),
            loadActText = LoadActText(repository),
            saveCase = SaveCase(repository),
            searchCase = SearchCase(repository),
            showSchedule = ShowSchedule(repository),
            updateCase = UpdateCase(repository)
        )
    }
}