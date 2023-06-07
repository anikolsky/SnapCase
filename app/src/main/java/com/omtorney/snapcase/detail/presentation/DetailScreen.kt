package com.omtorney.snapcase.detail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.presentation.components.CaseCard
import com.omtorney.snapcase.common.presentation.components.ErrorMessage
import com.omtorney.snapcase.common.presentation.components.LoadingIndicator

@Composable
fun DetailScreen(
    state: DetailState,
    accentColor: Color,
    onEvent: (DetailEvent) -> Unit,
    onActTextClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
//    LaunchedEffect(key1 = true) {
//        viewModel.eventFlow.collectLatest { event ->
//            when (event) {
//                UiEvent.Save -> {
//                    scaffoldState.snackbarHostState.showSnackbar(message = "Сохранено в избранное")
//                }
//                UiEvent.Delete -> {
//                    scaffoldState.snackbarHostState.showSnackbar(message = "Удалено из избранного")
//                }
//                is UiEvent.ShowSnackbar -> {
//                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
//                }
//            }
//        }
//    }

    Scaffold(bottomBar = {
        BottomAppBar {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (state.isFavorite) {
                            onEvent(DetailEvent.Delete(state.case))
                            onDismiss()
                        } else {
                            onEvent(DetailEvent.Save(state.case))
                        }
                    },
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = ButtonDefaults.buttonColors(accentColor),
                    enabled = !state.isLoading
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (state.isFavorite) {
                                R.drawable.ic_round_bookmark_remove
                            } else {
                                R.drawable.ic_round_bookmark_add
                            }
                        ),
                        contentDescription = "Add to favorites"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (state.isFavorite) {
                            stringResource(R.string.delete_favorite).uppercase()
                        } else {
                            stringResource(R.string.add_favorites).uppercase()
                        },
                    )
                }
            }
        }
    }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
                item {
                    Text(
                        text = "Карточка дела",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .fillMaxWidth()
                    )
                }
                item {
                    CaseCard(
                        case = state.case,
                        isExpanded = true,
                        accentColor = accentColor,
                        onCardClick = {},
                        onActTextClick = { onActTextClick(it) }
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item {
                    Text(
                        text = "Движение дела",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    )
                }
                items(state.case.process) { process ->
                    Card(
                        shape = MaterialTheme.shapes.extraSmall,
                        colors = CardDefaults.cardColors(accentColor.copy(alpha = 0.2f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
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
                if (state.case.appeal.isNotEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Актуальное обжалование",
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            )
                        }
                    }
                    item {
                        Card(
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(accentColor.copy(alpha = 0.2f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp)
                        ) {
                            Text(
                                text = state.case.appealToString(),
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
        if (state.isLoading) {
            LoadingIndicator(accentColor = accentColor)
        }
        if (state.error.isNotBlank()) {
            ErrorMessage(message = state.error)
        }
    }
}
