package com.omtorney.snapcase.domain.usecase.settings

import com.omtorney.snapcase.domain.Repository

class SetSelectedCourt(
    private val repository: Repository
) {
    suspend operator fun invoke(courtTitle: String) {
        repository.setSelectedCourt(courtTitle)
    }
}