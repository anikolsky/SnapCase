package com.omtorney.snapcase.favorites.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.presentation.components.BackButton
import com.omtorney.snapcase.common.presentation.components.BottomBar
import com.omtorney.snapcase.common.presentation.components.CaseColumn
import com.omtorney.snapcase.common.presentation.components.SettingsButton
import com.omtorney.snapcase.common.presentation.components.TopBar
import com.omtorney.snapcase.common.presentation.components.TopBarTitle
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.presentation.Screen

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: FavoritesState,
    onEvent: (FavoritesEvent) -> Unit,
    accentColor: Color,
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit,
    onCardClick: (Case) -> Unit,
    onActTextClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar {
                BackButton(
                    onBackClick = onBackClick
                )
                TopBarTitle(
                    title = Screen.Favorites.title,
                    modifier = Modifier.weight(1f)
                )
                SettingsButton(
                    onSettingsClick = onSettingsClick
                )
            }
        },
        bottomBar = { BottomBar(navController = navController) },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            CaseColumn(
                items = state.cases,
                accentColor = accentColor,
                onCardClick = { onCardClick(it) },
                onActTextClick = { onActTextClick(it) }
            )
            if (state.cases.isEmpty()) {
                Text(
                    text = stringResource(R.string.favorite_list_empty),
                    color = Color.Gray,
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
