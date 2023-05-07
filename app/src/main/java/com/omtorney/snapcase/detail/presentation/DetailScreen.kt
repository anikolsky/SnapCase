package com.omtorney.snapcase.detail.presentation

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.presentation.components.CaseCard
import com.omtorney.snapcase.common.presentation.components.UiEvent
import com.omtorney.snapcase.common.presentation.theme.Shapes
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailScreen(
    accentColor: Long,
    onActTextClick: (String) -> Unit,
    onDismiss: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel() // TODO move to NavHost
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                UiEvent.Save -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = "Сохранено в избранное")
                }
                UiEvent.Delete -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = "Удалено из избранного")
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
                        .padding(vertical = 2.dp)
                )
                state.case?.let { case ->
                    CaseCard(
                        case = case,
                        isExpanded = true,
                        accentColor = accentColor,
                        onCardClick = {},
                        onActTextClick = { onActTextClick(it) }
                    )
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Движение дела",
                                    style = MaterialTheme.typography.h6,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                )
                            }
                        }
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
                        if (case.appeal.isNotEmpty()) {
                            item {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "Последнее обжалование",
                                        style = MaterialTheme.typography.h6,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp)
                                    )
                                }
                            }
                            item {
                                Card(
                                    shape = Shapes.small,
                                    elevation = 6.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(2.dp)
                                ) {
                                    Text(
                                        text = case.appealToString(),
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 8.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        if (state.case!!.isFavorite) {
                            viewModel.onEvent(DetailEvent.Delete(state.case))
                            onDismiss()
                        } else {
                            viewModel.onEvent(DetailEvent.Save(state.case))
                        }
                    },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(Color(accentColor)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (state.case?.isFavorite == true) {
                            stringResource(R.string.delete_favorite).uppercase()
                        } else {
                            stringResource(R.string.add_favorites).uppercase()
                        },
                    )
                }
            }
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