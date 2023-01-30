package com.omtorney.snapcase.di

import android.content.Context
import androidx.room.Room
import com.omtorney.snapcase.data.database.AppDatabase
import com.omtorney.snapcase.data.database.CaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideCaseDao(appDatabase: AppDatabase): CaseDao {
        return appDatabase.caseDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "SnapCase_DB"
        ).build()
    }
}