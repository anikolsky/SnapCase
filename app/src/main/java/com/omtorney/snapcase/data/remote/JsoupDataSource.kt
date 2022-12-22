package com.omtorney.snapcase.data.remote

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class JsoupDataSource : RemoteDataSource {

    override suspend fun getJsoupDocument(url: String): Document {
        var document = Document("")
        try {
            document = Jsoup.connect(url).get()
        } catch (e: Exception) {
            Log.d("TESTLOG", "JsoupDataSource error: ${e.message}")
            // TODO: add crashlytics (see Notion)
            // TODO: В обработке ошибок добавить проверку на текст информация не доступна
        }
        return document
    }
}