package com.omtorney.snapcase.presentation.screen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omtorney.snapcase.data.model.News
import com.omtorney.snapcase.presentation.BottomBar
import com.omtorney.snapcase.presentation.viewmodel.NewsViewModel
import com.omtorney.snapcase.ui.theme.SnapCaseTheme
import kotlinx.coroutines.launch

@Composable
fun NewsScreen(
    navController: NavController,
    viewModel: NewsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val newsList by viewModel.news.collectAsState(listOf())
    viewModel.refreshNews()
    Scaffold(bottomBar = { BottomBar(navController = navController) }) { padding ->
        NewsList(
            newsList = newsList,
            Modifier.padding(padding)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsList(
    newsList: List<News>,
    modifier: Modifier = Modifier
) {

//    val refreshScope = rememberCoroutineScope()
//    var refreshing by remember { mutableStateOf(false) }
//
//    fun refresh() = refreshScope.launch {
//        refreshing = true
//        //...do something
//        refreshing = false
//    }

//    val state = rememberPullRefreshState(refreshing, ::refresh)

//    Box(modifier = Modifier.pullRefresh(state = state)) {
    LazyColumn(modifier = Modifier.background(MaterialTheme.colors.background)) {
//            if (!refreshing) {
        newsList.map { item { NewsItem(news = it) } }
//            }
    }
//        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
//    }
}

@Composable
fun NewsItem(
    news: News,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = 8.dp,
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Text(
                text = news.header,
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = news.preview
            )
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = news.date,
                    style = MaterialTheme.typography.overline
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NewsListPreview() {
    SnapCaseTheme {
        NewsList(
            listOf(
                News("News header", "News preview description", "21.12.2022"),
                News("News header", "News preview description", "21.12.2022"),
                News("News header", "News preview description", "21.12.2022")
            )
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NewsItemPreview() {
    SnapCaseTheme {
        NewsItem(news = News("News header", "News preview description", "21.12.2022"))
    }
}