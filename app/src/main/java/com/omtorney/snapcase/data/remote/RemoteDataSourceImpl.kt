package com.omtorney.snapcase.data.remote

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor() : RemoteDataSource {

    override suspend fun getJsoupDocument(url: String): Document? {
        return withContext(Dispatchers.IO) {
            try {
                Jsoup.connect(url).get()
            } catch (e: Exception) {
                Log.d("RemoteDataSourceImpl", "error: ${e.message}")
                // TODO add crashlytics
                // TODO В обработке ошибок добавить проверку на текст информация не доступна
                null
            }
        }
    }
}