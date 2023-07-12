package com.omtorney.snapcase.common.data

import com.omtorney.snapcase.common.data.local.LocalDataSource
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.data.remote.RemoteDataSource
import com.omtorney.snapcase.common.domain.Repository
import kotlinx.coroutines.flow.Flow
import org.jsoup.nodes.Document
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {

    override suspend fun getJsoupDocument(url: String): Document? {
        return remoteDataSource.getJsoupDocument(url)
    }

    override suspend fun saveCase(case: Case) {
        localDataSource.saveCase(case)
    }

    override suspend fun deleteFavorite(case: Case) {
        localDataSource.deleteFavorite(case)
    }

    override suspend fun checkCase(uid: String): Int {
        return localDataSource.checkCase(uid)
    }

    override suspend fun getCaseByNumber(number: String): Case? {
        return localDataSource.getCaseByNumber(number)
    }

    override fun getFavoriteCases(): Flow<List<Case>> {
        return localDataSource.getFavoriteCases()
    }
}
