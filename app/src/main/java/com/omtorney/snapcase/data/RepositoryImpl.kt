package com.omtorney.snapcase.data

import com.omtorney.snapcase.data.local.LocalDataSource
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.data.remote.RemoteDataSource
import com.omtorney.snapcase.domain.Repository
import org.jsoup.nodes.Document
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {

    override suspend fun getHtmlData(url: String): String {
        return remoteDataSource.getJsoupDocument(url).toString()
    }

    override suspend fun getJsoupDocument(url: String): Document? {
        return remoteDataSource.getJsoupDocument(url)
    }

    override suspend fun deleteFavorite(case: Case) {
        localDataSource.deleteFavorite(case)
    }

    override suspend fun addFavorite(case: Case) {
        localDataSource.addFavorite(case)
    }

    override suspend fun updateFavorite(case: Case) {
        localDataSource.updateFavorite(case)
    }

    override suspend fun checkCase(uid: String): Int {
        return localDataSource.checkCase(uid)
    }
}