package com.omtorney.snapcase.common.di

import com.omtorney.snapcase.common.data.RepositoryImpl
import com.omtorney.snapcase.common.data.local.LocalDataSource
import com.omtorney.snapcase.common.data.local.LocalDataSourceImpl
import com.omtorney.snapcase.common.data.remote.RemoteDataSource
import com.omtorney.snapcase.common.data.remote.RemoteDataSourceImpl
import com.omtorney.snapcase.common.domain.Repository
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
    abstract fun bindRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource
}