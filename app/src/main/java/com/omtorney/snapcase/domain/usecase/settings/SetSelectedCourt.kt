package com.omtorney.snapcase.domain.usecase.settings

import com.omtorney.snapcase.domain.repository.Repository
import javax.inject.Inject

class SetSelectedCourt @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(courtTitle: String) {
        repository.setSelectedCourt(courtTitle)
    }
}
