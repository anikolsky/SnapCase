package com.omtorney.snapcase.data

import com.omtorney.snapcase.data.local.LocalDataSource
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.data.remote.RemoteDataSource
import com.omtorney.snapcase.domain.Repository
import com.omtorney.snapcase.util.Constants
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

    override suspend fun updateFavorite(case: Case) {
        localDataSource.updateFavorite(case)
    }

    override suspend fun checkCase(uid: String): Int {
        return localDataSource.checkCase(uid)
    }

    override suspend fun clearRecentCases() {
        localDataSource.clearRecentCases()
    }

    override suspend fun getCaseByNumber(number: String): Case? {
        return localDataSource.getCaseByNumber(number)
    }

    override fun getFavoriteCases(): Flow<List<Case>> {
        return localDataSource.getFavoriteCases()
    }

    override fun getRecentCases(): Flow<List<Case>> {
        return localDataSource.getRecentCases()
    }

    override val getAccentColor: Flow<Long> = localDataSource.getAccentColor

    override suspend fun setAccentColor(color: Long) = localDataSource.setAccentColor(color)

    override val getInitialColor: Long = localDataSource.getInitialColor
}