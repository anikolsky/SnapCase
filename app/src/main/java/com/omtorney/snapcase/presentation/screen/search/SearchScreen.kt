package com.omtorney.snapcase.presentation.screen.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.presentation.components.CaseColumn
import com.omtorney.snapcase.presentation.components.ErrorMessage
import com.omtorney.snapcase.presentation.components.LoadingIndicator

@Composable
fun SearchScreen(
    state: SearchState,
    accentColor: Color,
    onCardClick: (Case) -> Unit,
    onActTextClick: (Case) -> Unit
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            CaseColumn(
                items = state.cases,
                accentColor = accentColor,
                onCardClick = { onCardClick(it) },
                onActTextClick = { onActTextClick(it) }
            )
        }
        if (state.isLoading) {
            LoadingIndicator(accentColor = accentColor)
        }
        if (state.error.isNotBlank()) {
            ErrorMessage(message = state.error)
        }
    }
}
