package com.omtorney.snapcase.data

import com.omtorney.snapcase.data.local.LocalDataSource
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.data.remote.RemoteDataSource
import com.omtorney.snapcase.domain.repository.Repository
import com.omtorney.snapcase.data.remote.database.FirestoreResult
import com.omtorney.snapcase.data.remote.database.FirestoreUser
import com.omtorney.snapcase.presentation.screen.settings.CheckPeriod
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

    override suspend fun deleteCase(case: Case) {
        localDataSource.deleteCase(case)
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


    override val accentColor: Flow<Long> = localDataSource.accentColor

    override suspend fun setAccentColor(color: Long) {
        localDataSource.setAccentColor(color)
    }

    override val isDarkThemeEnabled: Flow<Boolean> = localDataSource.isDarkThemeEnabled

    override suspend fun enableDarkTheme(enabled: Boolean) {
        localDataSource.enableDarkTheme(enabled)
    }

    override val selectedCourt: Flow<String> = localDataSource.selectedCourt

    override suspend fun setSelectedCourt(courtTitle: String) {
        localDataSource.setSelectedCourt(courtTitle)
    }

    override val caseCheckPeriod: Flow<CheckPeriod> = localDataSource.caseCheckPeriod

    override suspend fun setCaseCheckPeriod(period: CheckPeriod) {
        localDataSource.setCaseCheckPeriod(period)
    }


    override fun getFirestoreUser(userName: String): Flow<FirestoreResult<List<FirestoreUser?>>> {
        return remoteDataSource.getFirestoreUser(userName)
    }

    override suspend fun createFirestoreBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> {
        return remoteDataSource.createFirestoreBackup(firestoreUser)
    }

    override suspend fun updateFirestoreBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> {
        return remoteDataSource.updateFirestoreBackup(firestoreUser)
    }

    override suspend fun deleteFirestoreBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> {
        return remoteDataSource.updateFirestoreBackup(firestoreUser)
    }
}
