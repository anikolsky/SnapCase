package com.omtorney.snapcase.presentation.favorites

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omtorney.snapcase.R
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.presentation.Screen
import com.omtorney.snapcase.presentation.common.*

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    accentColor: Long,
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit,
    onCardClick: (Case) -> Unit,
    onActTextClick: (String) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel() // TODO move to NavHost
) {
    val state = viewModel.state.value

    Scaffold(
        topBar = {
            TopBar {
                BackButton(
                    accentColor = accentColor,
                    onBackClick = onBackClick
                )
                TopBarTitle(
                    title = Screen.Favorites.title,
                    accentColor = accentColor,
                    modifier = Modifier.weight(1f)
                )
                SettingsButton(
                    accentColor = accentColor,
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