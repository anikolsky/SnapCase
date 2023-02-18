package com.omtorney.snapcase.presentation.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omtorney.snapcase.R
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.presentation.common.CaseCard
import com.omtorney.snapcase.presentation.common.UiEvent
import com.omtorney.snapcase.presentation.ui.theme.Shapes
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailScreen(
    onActTextClick: (String) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                UiEvent.Save -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = "Сохранено в избранное")
                }
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column {
                Text(
                    text = "Карточка дела",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp, bottom = 0.dp)
                )
                state.case?.let { case ->
                    CaseCard(
                        case = case,
                        isExpanded = true,
                        onCardClick = {},
                        onActTextClick = { onActTextClick(it) }
                    )
                    Text(
                        text = "Движение дела",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 8.dp)
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
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 8.dp
                                        )
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
                    onClick = {
                        if (state.case!!.isFavorite) {
                            viewModel.onEvent(DetailEvent.Delete(state.case))
                        } else {
                            viewModel.onEvent(DetailEvent.Save(state.case))
                        }
                    },
                    shape = RectangleShape,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (state.case?.isFavorite == true) {
                            stringResource(R.string.delete_favorite).uppercase()
                        } else {
                            stringResource(R.string.save_favorites).uppercase()
                        }
                    )
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
}