package com.omtorney.snapcase.domain.usecase

import android.util.Log
import com.omtorney.snapcase.domain.Repository
import com.omtorney.snapcase.domain.court.CaseType
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.parser.PageParserFactory
import com.omtorney.snapcase.util.Resource
import com.omtorney.snapcase.util.handleException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import javax.inject.Inject

class SearchCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(
        courtTitle: String,
        caseType: CaseType,
        query: String
    ): Flow<Resource<List<Case>>> = flow {
        try {
            emit(Resource.Loading())
            var sideName = ""
            var caseNumber = ""
            if (query.contains("[а-яА-Яa-zA-Z]".toRegex()))
                sideName = query
            else
                caseNumber = query
            Log.d("TESTLOG", "SearchCase: sideName: $sideName, caseNumber: $caseNumber")
            val court = Courts.getCourtList().find { it.title == courtTitle } ?: Courts.Dmitrov
            Log.d("TESTLOG", "SearchCase: court: ${court.title}")
            val searchUrl = withContext(Dispatchers.IO) {
                court.getSearchQuery(
                    caseType,
                    URLEncoder.encode(sideName, "cp1251"),
                    URLEncoder.encode(caseNumber, "cp1251")
                )
            }
            Log.d("TESTLOG", "SearchCase: searchUrl: $searchUrl")
            val html = repository.getJsoupDocument(searchUrl).toString()
            Log.d("TESTLOG", "SearchCase: fetching page...")
            val page = PageParserFactory(repository).create(court)
            Log.d("TESTLOG", "SearchCase: fetching result...")
            val result = page.extractSearchResult(html, court)
            Log.d("TESTLOG", "SearchCase: result: $result")
            emit(Resource.Success(result))
        } catch (e: Throwable) {
            emit(Resource.Error(message = handleException(e)))
            Log.d("TESTLOG", "SearchCase: error: ${handleException(e)}")
        }
    }
}