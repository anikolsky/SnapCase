package com.omtorney.snapcase.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import androidx.work.WorkManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.omtorney.snapcase.data.local.database.AppDatabase
import com.omtorney.snapcase.data.local.database.CaseDao
import com.omtorney.snapcase.data.local.datastore.SettingsStore
import com.omtorney.snapcase.util.Constants.DATA_STORE_NAME
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
    fun provideSettingsStore(dataStore: DataStore<Preferences>): SettingsStore {
        return SettingsStore(dataStore)
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

    @Provides
    @Singleton
    fun provideFirestoreDatabaseReference(): CollectionReference {
        return Firebase.firestore.collection("users")
    }
}
