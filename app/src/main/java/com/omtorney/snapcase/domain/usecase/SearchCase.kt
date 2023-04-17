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
            val court = Courts.getCourtList().find { it.title == courtTitle } ?: Courts.Dmitrov
//            Log.d("TESTLOG", "[SearchCase] val sideName: \"$sideName\", val caseNumber: \"$caseNumber\", court.title: \"${court.title}\"")
            val searchUrl = withContext(Dispatchers.IO) {
                court.getSearchQuery(
                    caseType,
                    URLEncoder.encode(sideName, "cp1251"),
                    URLEncoder.encode(caseNumber, "cp1251")
                )
            }
//            Log.d("TESTLOG", "[SearchCase] val searchUrl: $searchUrl")
            val document = repository.getJsoupDocument(searchUrl)
//            Log.d("TESTLOG", "[SearchCase] val document: ${document!!.select("div[id=content]")}")
//            Log.d("TESTLOG", "[SearchCase] fetching pageParser...")
            val pageParser = PageParserFactory(repository).create(court)
//            Log.d("TESTLOG", "[SearchCase] fetching List<Case>...")
            val caseList = pageParser.extractSearchResult(document!!, court)
//            Log.d("TESTLOG", "[SearchCase] caseList: $caseList")
            emit(Resource.Success(data = caseList))
        } catch (e: Throwable) {
            Log.d("TESTLOG", "[SearchCase] exception: ${e.localizedMessage}")
            emit(Resource.Error(message = handleException(e)))
        }
    }
}