package com.omtorney.snapcase.domain.repository

import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.data.remote.database.FirestoreResult
import com.omtorney.snapcase.data.remote.database.FirestoreUser
import com.omtorney.snapcase.presentation.screen.settings.CheckPeriod
import kotlinx.coroutines.flow.Flow
import org.jsoup.nodes.Document

interface Repository {

    suspend fun getJsoupDocument(url: String): Document?

    suspend fun saveCase(case: Case)
    suspend fun deleteCase(case: Case)
    suspend fun checkCase(uid: String): Int
    suspend fun getCaseByNumber(number: String): Case?
    fun getFavoriteCases(): Flow<List<Case>>

    val accentColor: Flow<Long>
    suspend fun setAccentColor(color: Long)
    val isDarkThemeEnabled: Flow<Boolean>
    suspend fun enableDarkTheme(enabled: Boolean)
    val selectedCourt: Flow<String>
    suspend fun setSelectedCourt(courtTitle: String)
    val caseCheckPeriod: Flow<CheckPeriod>
    suspend fun setCaseCheckPeriod(period: CheckPeriod)

    fun getFirestoreUser(userName: String): Flow<FirestoreResult<FirestoreUser?>>
    suspend fun createFirestoreBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>>
    suspend fun updateFirestoreBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>>
    suspend fun deleteFirestoreBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>>
}
