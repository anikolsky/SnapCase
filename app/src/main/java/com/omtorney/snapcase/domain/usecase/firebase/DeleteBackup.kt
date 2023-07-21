package com.omtorney.snapcase.domain.usecase.firebase

import com.omtorney.snapcase.domain.repository.Repository
import com.omtorney.snapcase.data.remote.database.FirestoreResult
import com.omtorney.snapcase.data.remote.database.FirestoreUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteBackup @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> {
        return repository.deleteFirestoreBackup(firestoreUser)
    }
}
