package com.omtorney.snapcase.data.remote

import com.omtorney.snapcase.data.remote.database.FirestoreResult
import com.omtorney.snapcase.data.remote.database.FirestoreUser
import kotlinx.coroutines.flow.Flow
import org.jsoup.nodes.Document

interface RemoteDataSource {

    suspend fun getJsoupDocument(url: String): Document?

    fun getFirestoreUser(userName: String): Flow<FirestoreResult<List<FirestoreUser?>>>
    suspend fun createFirestoreBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>>
    suspend fun updateFirestoreBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>>
    suspend fun deleteFirestoreBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>>
}
