package com.omtorney.snapcase.search.domain.usecase

import com.omtorney.snapcase.common.domain.Repository
import com.omtorney.snapcase.common.domain.court.Courts
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.model.CaseType
import com.omtorney.snapcase.common.domain.parser.PageParserFactory
import com.omtorney.snapcase.common.presentation.logd
import com.omtorney.snapcase.common.util.Resource
import com.omtorney.snapcase.common.util.handleException
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
            val court = Courts.getCourt(courtTitle)
            val searchUrl = withContext(Dispatchers.IO) {
                court.getSearchQuery(
                    caseType,
                    URLEncoder.encode(sideName, "cp1251"),
                    URLEncoder.encode(caseNumber, "cp1251")
                )
            }
            val document = repository.getJsoupDocument(searchUrl)
            val pageParser = PageParserFactory(repository).create(court)
            val caseList = pageParser.extractSearchResult(document!!, court)
            emit(Resource.Success(data = caseList))
        } catch (e: Exception) {
            logd("SearchCase error: ${e.message}")
            emit(Resource.Error(message = handleException(e)))
        }
    }
}
