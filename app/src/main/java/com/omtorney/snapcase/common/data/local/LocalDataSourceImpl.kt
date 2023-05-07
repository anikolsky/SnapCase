package com.omtorney.snapcase.common.data.local

import com.omtorney.snapcase.common.data.database.CaseDao
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.settings.data.SettingsStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val caseDao: CaseDao,
    private val settingsStore: SettingsStore
) : LocalDataSource {

    override suspend fun saveCase(case: Case) {
        caseDao.insert(case)
    }

    override suspend fun deleteFavorite(case: Case) {
        caseDao.delete(case)
    }

    override suspend fun updateFavorite(case: Case) {
        caseDao.update(case)
    }

    override suspend fun checkCase(uid: String): Int {
        return caseDao.check(uid)
    }

    override suspend fun clearRecentCases() {
        caseDao.deleteRecent()
    }

    override suspend fun getCaseByNumber(number: String): Case? {
        return caseDao.getCaseByNumber(number)
    }

    override fun getFavoriteCases(): Flow<List<Case>> {
        return caseDao.getFavorites()
    }

    override fun getRecentCases(): Flow<List<Case>> {
        return caseDao.getRecent()
    }

    override val getAccentColor: Flow<Long>
        get() = settingsStore.getAccentColor

    override suspend fun setAccentColor(color: Long) {
        settingsStore.setAccentColor(color)
    }

    override val getSelectedCourt: Flow<String>
        get() = settingsStore.getSelectedCourt

    override suspend fun setSelectedCourt(courtTitle: String) {
        settingsStore.setSelectedCourt(courtTitle)
    }
}