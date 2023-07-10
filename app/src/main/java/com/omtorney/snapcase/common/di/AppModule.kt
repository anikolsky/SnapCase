package com.omtorney.snapcase.common.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import androidx.work.WorkManager
import com.omtorney.snapcase.common.data.database.AppDatabase
import com.omtorney.snapcase.common.data.database.CaseDao
import com.omtorney.snapcase.common.util.Constants.DATA_STORE_NAME
import com.omtorney.snapcase.network.NetworkStateManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "SnapCase_DB"
        ).build()
    }

    @Provides
    fun provideCaseDao(appDatabase: AppDatabase): CaseDao {
        return appDatabase.caseDao()
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            appContext.preferencesDataStoreFile(DATA_STORE_NAME)
        }
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext appContext: Context): WorkManager {
        return WorkManager.getInstance(appContext)
    }

    @Provides
    @Singleton
    fun provideNetworkStateManager(@ApplicationContext appContext: Context): NetworkStateManager {
        return NetworkStateManager(appContext)
    }
}
