package com.omtorney.snapcase.di

import android.content.Context
import androidx.room.Room
import com.omtorney.snapcase.data.RepositoryImpl
import com.omtorney.snapcase.data.database.AppDatabase
import com.omtorney.snapcase.data.database.CaseDao
import com.omtorney.snapcase.data.local.LocalDataSource
import com.omtorney.snapcase.data.local.RoomDataSource
import com.omtorney.snapcase.data.remote.JsoupDataSource
import com.omtorney.snapcase.data.remote.RemoteDataSource
import com.omtorney.snapcase.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// TODO разбить на разные модули, везде ли нужен сигнлтон, как изменить InstallIn

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): Repository {
        return RepositoryImpl(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(): RemoteDataSource {
        return JsoupDataSource()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(caseDao: CaseDao): LocalDataSource {
        return RoomDataSource(caseDao)
    }

    @Provides
    fun provideCaseDao(appDatabase: AppDatabase): CaseDao {
        return appDatabase.caseDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java,"SnapCase_DB").build()
    }
}