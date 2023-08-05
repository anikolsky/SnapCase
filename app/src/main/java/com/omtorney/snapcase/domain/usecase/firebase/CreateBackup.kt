package com.omtorney.snapcase.domain.usecase.firebase

import com.omtorney.snapcase.domain.repository.Repository
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.data.remote.database.FirestoreResult
import com.omtorney.snapcase.data.remote.database.FirestoreUser
import com.omtorney.snapcase.presentation.logd
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class CreateBackup @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(firestoreUserName: String, cases: List<Case>): Flow<FirestoreResult<String>> {
        val casesJson = Json.encodeToString(cases)
        logd("cases: $casesJson")
        val firestoreUser = FirestoreUser(name = firestoreUserName, cases = casesJson)
        return repository.createFirestoreBackup(firestoreUser)
    }
}
