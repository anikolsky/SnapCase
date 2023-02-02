package com.omtorney.snapcase.presentation.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omtorney.snapcase.R
import com.omtorney.snapcase.presentation.common.CaseCard
import com.omtorney.snapcase.presentation.ui.theme.Shapes

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Box {
        Column {
            state.case?.let { case ->
                CaseCard(
                    case = case,
                    isExpanded = true,
                    onCardClick = {},
                    onActTextClick = {}
                )
                LazyColumn(modifier = Modifier.weight(1f)) {
                    case.process.map { process ->
                        item {
                            Card(
                                shape = Shapes.small,
                                elevation = 6.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp)
                            ) {
                                Text(
                                    text = process.toString(),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                    item {
                        case.appealToString()
                    }
                }
            }
            Button(
                onClick = { viewModel.onEvent(DetailEvent.Save(state.case!!)) },
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.save_favorites))
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