package com.omtorney.snapcase.common.data.local

import com.omtorney.snapcase.common.data.database.CaseDao
import com.omtorney.snapcase.common.domain.model.Case
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val caseDao: CaseDao
) : LocalDataSource {

    override suspend fun saveCase(case: Case) {
        caseDao.insert(case)
    }

    override suspend fun deleteFavorite(case: Case) {
        caseDao.delete(case)
    }

    override suspend fun checkCase(number: String): Int {
        return caseDao.checkCase(number)
    }

//    override suspend fun clearRecentCases() {
//        caseDao.deleteRecentCases()
//    }

    override suspend fun getCaseByNumber(number: String): Case? {
        return caseDao.getCaseByNumber(number)
    }

    override fun getFavoriteCases(): Flow<List<Case>> {
        return caseDao.getFavoriteCases()
    }

//    override fun getRecentCases(): Flow<List<Case>> {
//        return caseDao.getRecentCases()
//    }
}
