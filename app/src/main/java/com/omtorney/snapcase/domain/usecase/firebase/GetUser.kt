package com.omtorney.snapcase.domain.usecase.firebase

import com.omtorney.snapcase.data.remote.database.FirestoreResult
import com.omtorney.snapcase.data.remote.database.FirestoreUser
import com.omtorney.snapcase.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUser @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(userName: String): Flow<FirestoreResult<FirestoreUser?>> {
        return repository.getFirestoreUser(userName)
    }
}
