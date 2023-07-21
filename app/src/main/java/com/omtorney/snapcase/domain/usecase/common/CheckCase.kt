package com.omtorney.snapcase.domain.usecase.common

import com.omtorney.snapcase.domain.repository.Repository
import javax.inject.Inject

class CheckCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(uid: String): Boolean {
        return repository.checkCase(uid) > 0
    }
}
