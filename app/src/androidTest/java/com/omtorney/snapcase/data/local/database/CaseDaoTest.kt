package com.omtorney.snapcase.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.omtorney.snapcase.domain.model.Case
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CaseDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var caseDao: CaseDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        caseDao = db.caseDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertCaseAndGetByNumber() = runBlocking {
        val case = Case("2-2222/22", "123abc", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
        caseDao.upsert(case)

        val byNumber = caseDao.getCaseByNumber("2-2222/22")

        assert(byNumber == case)
    }

    @Test
    fun updateCaseAndGetByNumber() = runBlocking {
        val case = Case("2-2222/22", "123abc", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
        caseDao.upsert(case)

        caseDao.upsert(case.copy(number = "2-2223/22"))
        val byNumber = caseDao.getCaseByNumber("2-2223/22")

        assert(byNumber != case && byNumber?.number == "2-2223/22")
    }

    @Test
    fun checkCase() = runBlocking {
        val case = Case("2-2222/22", "123abc", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
        caseDao.upsert(case)

        val caseCount = caseDao.checkCase("123abc")

        assert(caseCount > 0)
    }

    @Test
    fun deleteCase() = runBlocking {
        val case = Case("2-2222/22", "123abc", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
        caseDao.upsert(case)

        caseDao.delete(case)
        val cases = caseDao.getFavoriteCases().first()

        assert(cases.isEmpty())
    }
}
