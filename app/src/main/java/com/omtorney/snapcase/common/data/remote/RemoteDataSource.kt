package com.omtorney.snapcase.common.data.remote

import org.jsoup.nodes.Document

interface RemoteDataSource {

    suspend fun getJsoupDocument(url: String): Document?
}