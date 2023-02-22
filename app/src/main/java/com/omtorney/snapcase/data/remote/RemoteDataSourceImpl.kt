package com.omtorney.snapcase.data.remote

import android.util.Log
import com.omtorney.snapcase.util.NoResultFound
import com.omtorney.snapcase.util.SiteDataUnavailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor() : RemoteDataSource {

    override suspend fun getJsoupDocument(url: String): Document? {
        return withContext(Dispatchers.IO) {
            try {
                val document = Jsoup.connect(url).get()
                if (document.text().contains("Информация временно недоступна. Приносим свои извинения"))
                    throw SiteDataUnavailable()
                else if (document.text().contains("Данных по запросу не обнаружено") ||
                    document.text().contains("По вашему запросу ничего не найдено"))
                    throw NoResultFound()
                else document
            } catch (e: Exception) {
                Log.d("RemoteDataSourceImpl", "error: ${e.message}")
                null
            }
        }
    }
}