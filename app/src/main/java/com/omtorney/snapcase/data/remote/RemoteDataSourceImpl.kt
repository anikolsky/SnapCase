package com.omtorney.snapcase.data.remote

import android.util.Log
import com.omtorney.snapcase.util.NoResultFound
import com.omtorney.snapcase.util.NoScheduledCases
import com.omtorney.snapcase.util.SiteDataUnavailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor() : RemoteDataSource {

    override suspend fun getJsoupDocument(url: String): Document {
        return withContext(Dispatchers.IO) {
            try {
                val document = Jsoup.connect(url).get()
                Log.d("TESTLOG", "RemoteDataSourceImpl: document: ${document.title()}")
                if (document.text().contains("Информация временно недоступна. Приносим свои извинения")) {
                    Log.d("TESTLOG", "RemoteDataSourceImpl: Информация временно недоступна. Приносим свои извинения")
                    throw SiteDataUnavailable()
                }
                else if (document.text().contains("Данных по запросу не обнаружено") ||
                    document.text().contains("По вашему запросу ничего не найдено")) {
                    Log.d("TESTLOG", "RemoteDataSourceImpl: Данных по запросу не обнаружено || По вашему запросу ничего не найдено")
                    throw NoResultFound()
                }
                else if (document.text().contains("дел не назначено")) {
                    Log.d("TESTLOG", "RemoteDataSourceImpl: На выбранную дату дел не назначено")
                    throw NoScheduledCases()
                }
                else {
                    Log.d("TESTLOG", "RemoteDataSourceImpl: RESULTS FOUND!")
                    document
                }
            } catch (e: Exception) {
                Log.d("TESTLOG", "RemoteDataSourceImpl: error: ${e.localizedMessage}")
                Document("")
            }
        }
    }
}