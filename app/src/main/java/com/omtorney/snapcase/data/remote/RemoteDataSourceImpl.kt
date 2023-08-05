package com.omtorney.snapcase.data.remote

import com.omtorney.snapcase.presentation.logd
import com.omtorney.snapcase.data.remote.database.FirestoreDatabase
import com.omtorney.snapcase.data.remote.database.FirestoreResult
import com.omtorney.snapcase.data.remote.database.FirestoreUser
import com.omtorney.snapcase.util.CustomError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val firestoreDatabase: FirestoreDatabase
) : RemoteDataSource {

    override suspend fun getJsoupDocument(url: String): Document? {
        return withContext(Dispatchers.IO) {
            val document = Jsoup.connect(url).get()
            if (document.text().contains("Информация временно недоступна. Приносим свои извинения")) {
                logd("Информация временно недоступна. Приносим свои извинения")
                throw CustomError.SiteDataUnavailable()
            } else if (document.text().contains("Данных по запросу не обнаружено") ||
                document.text().contains("По вашему запросу ничего не найдено")
            ) {
                logd("Данных по запросу не обнаружено || По вашему запросу ничего не найдено")
                throw CustomError.NoResultFound()
            } else if (document.text().contains("дел не назначено")) {
                logd("На выбранную дату дел не назначено")
                throw CustomError.NoScheduledCases()
            } else if (document.text().contains("Этот запрос заблокирован по соображениям безопасности")) {
                logd("Этот запрос заблокирован по соображениям безопасности")
                throw CustomError.BlockedAccess()
            } else {
                document
            }
        }
    }

    override fun getFirestoreUser(userName: String): Flow<FirestoreResult<FirestoreUser?>> {
        return firestoreDatabase.getFirestoreUser(userName)
    }

    override suspend fun createFirestoreBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> {
        return firestoreDatabase.createFirestoreBackup(firestoreUser)
    }

    override suspend fun updateFirestoreBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> {
        return firestoreDatabase.updateFirestoreBackup(firestoreUser)
    }

    override suspend fun deleteFirestoreBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> {
        return firestoreDatabase.deleteFirestoreBackup(firestoreUser)
    }
}
