package com.omtorney.snapcase.common.data.remote

import com.omtorney.snapcase.common.presentation.logd
import com.omtorney.snapcase.common.util.CustomError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor() : RemoteDataSource {

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
            } else {
                document
            }
        }
    }
}
