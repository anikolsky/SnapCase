package com.omtorney.snapcase.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.presentation.common.BottomBar
import com.omtorney.snapcase.presentation.common.CaseColumn

@Composable
fun SearchScreen(
    navController: NavController,
    accentColor: Long,
    onCardClick: (Case) -> Unit,
    onActTextClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel() // TODO move to NavHost
) {
    val state = viewModel.state.value

    Scaffold(bottomBar = { BottomBar(navController = navController) }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            CaseColumn(
                items = state.cases,
                accentColor = accentColor,
                onCardClick = { case ->
                    viewModel.cacheCase(case)
                    onCardClick(case)
                },
                onActTextClick = { onActTextClick(it) }
            )
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = Color(accentColor),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            if (state.error.isNotBlank()) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}