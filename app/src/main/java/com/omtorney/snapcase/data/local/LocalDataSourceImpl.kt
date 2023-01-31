package com.omtorney.snapcase.data.local

import com.omtorney.snapcase.data.database.CaseDao
import com.omtorney.snapcase.domain.model.Case
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val caseDao: CaseDao
) : LocalDataSource {

    override fun getFavoriteCases(): Flow<List<Case>> {
        return caseDao.getAll()
    }

    override suspend fun deleteFavorite(case: Case) {
        caseDao.delete(case)
    }

    override suspend fun addFavorite(case: Case) {
        caseDao.insert(case)
    }

    override suspend fun updateFavorite(case: Case) {
        caseDao.update(case)
    }

    override suspend fun checkCase(uid: String): Int {
        return caseDao.check(uid)
    }
}