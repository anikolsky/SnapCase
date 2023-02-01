package com.omtorney.snapcase.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omtorney.snapcase.presentation.common.CaseCard

@Composable
fun DetailScreen(
//    caseParam: Case,
//    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
//    viewModel.onEvent(event = DetailEvent.Fill(caseParam))

    // TODO add flag when favorite case

    val state = viewModel.state.value

    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = MaterialTheme.colors.surface)
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 0.dp)
    ) {
        Column {
            state.case?.let { case ->
                CaseCard(
                    case = case,
                    onCardClick = {},
                    onActTextClick = {}
                )
                LazyColumn {
                    case.process.map {
                        item {
                            Card {
                                Text(text = it.toString())
                            }
                        }
                    }
                }
                LazyColumn {
                    case.appealToString()
                }
            }
        }
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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