package com.omtorney.snapcase.domain.usecase

import android.util.Log
import com.omtorney.snapcase.domain.Repository
import com.omtorney.snapcase.domain.court.CaseType
import com.omtorney.snapcase.domain.court.Court
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
            val court = Courts.getCourtList().find { it.title == courtTitle } ?: Courts.Dmitrov
            val searchUrl = withContext(Dispatchers.IO) {
                court.getSearchQuery(
                    caseType,
                    URLEncoder.encode(sideName, "cp1251"),
                    URLEncoder.encode(caseNumber, "cp1251")
                )
            }
            val html = repository.getJsoupDocument(searchUrl).toString()
            val page = PageParserFactory(repository).create(court)
            val result = page.extractSearchResult(html, court)
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(message = handleException(e)))
            Log.d("TESTLOG", "SearchCase: error: ${e.localizedMessage}")
        }
    }
}