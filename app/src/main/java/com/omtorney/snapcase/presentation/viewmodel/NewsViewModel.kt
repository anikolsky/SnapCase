package com.omtorney.snapcase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.data.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor() : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _news = MutableStateFlow(listOf<News>())
    val news = _news.asStateFlow()

//    init {
//        loadNews()
//    }

    private fun loadNews() = viewModelScope.launch {
        _isLoading.value = true
        val newsList = mutableListOf<News>()
        val page = withContext(Dispatchers.IO) {
            Jsoup.connect("https://pravo.ru/news/").get() // TODO repository and data source
        }
        val newsBlock = page.select("div[id=w0]")
        val newsHeaders = newsBlock.select("header[class=block_header]")
        val newsDate = newsBlock.select("div[class=date muted]")
        val newsPreviews = newsBlock.select("article")
        for (i in newsHeaders.indices) {
            newsList.add(News(newsHeaders[i].text(), newsPreviews[i].text(), newsDate[i].text()))
        }
        _news.value = newsList
        _isLoading.value = false
    }

    fun refreshNews() {
        loadNews()
    }
}