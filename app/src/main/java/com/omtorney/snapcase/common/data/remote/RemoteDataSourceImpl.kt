package com.omtorney.snapcase.common.data.remote

import com.omtorney.snapcase.common.presentation.logd
import com.omtorney.snapcase.common.util.NoResultFound
import com.omtorney.snapcase.common.util.NoScheduledCases
import com.omtorney.snapcase.common.util.SiteDataUnavailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor() : RemoteDataSource {

    override suspend fun getJsoupDocument(url: String): Document? {
        return withContext(Dispatchers.IO) {
            try {
//                logd("url: $url")
                val document = Jsoup.connect(url).get()
//                logd("val document: ${document!!.select("div[id=content]")}")
                if (document.text().contains("Информация временно недоступна. Приносим свои извинения")) {
                    logd("Информация временно недоступна. Приносим свои извинения")
                    throw SiteDataUnavailable()
                }
                else if (document.text().contains("Данных по запросу не обнаружено") ||
                    document.text().contains("По вашему запросу ничего не найдено")) {
                    logd("Данных по запросу не обнаружено || По вашему запросу ничего не найдено")
                    throw NoResultFound()
                }
                else if (document.text().contains("дел не назначено")) {
                    logd("На выбранную дату дел не назначено")
                    throw NoScheduledCases()
                }
                else {
//                    logd("CASES FOUND!")
                    document
                }
            } catch (e: Throwable) {
                logd("exception: ${e.localizedMessage}")
                null
            }
        }
    }
}
