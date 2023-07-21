package com.omtorney.snapcase.di

import com.omtorney.snapcase.data.RepositoryImpl
import com.omtorney.snapcase.data.local.LocalDataSource
import com.omtorney.snapcase.data.local.LocalDataSourceImpl
import com.omtorney.snapcase.data.remote.RemoteDataSource
import com.omtorney.snapcase.data.remote.RemoteDataSourceImpl
import com.omtorney.snapcase.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource
}
