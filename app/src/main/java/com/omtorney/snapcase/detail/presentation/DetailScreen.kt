package com.omtorney.snapcase.detail.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.presentation.components.CaseCard

@Composable
fun DetailScreen(
    state: DetailState,
    onEvent: (DetailEvent) -> Unit,
    accentColor: Color,
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

    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = "Карточка дела",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 6.dp)
                )
                state.case.let { case ->
                    CaseCard(
                        case = case,
                        isExpanded = true,
                        accentColor = accentColor,
                        onCardClick = {},
                        onActTextClick = { onActTextClick(it) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Движение дела",
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 6.dp)
                                )
                            }
                        }
                        case.process.map { process ->
                            item {
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
                        }
                        if (case.appeal.isNotEmpty()) {
                            item {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "Последнее обжалование",
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
                        if (state.case.isFavorite) {
                            onEvent(DetailEvent.Delete(state.case))
                            onDismiss()
                        } else {
                            onEvent(DetailEvent.Save(state.case))
                        }
                    },
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = ButtonDefaults.buttonColors(accentColor),
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (state.case.isFavorite) {
                            stringResource(R.string.delete_favorite).uppercase()
                        } else {
                            stringResource(R.string.add_favorites).uppercase()
                        },
                    )
                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = accentColor,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            if (state.error.isNotBlank()) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
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
