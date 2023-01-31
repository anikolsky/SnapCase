package com.omtorney.snapcase.presentation.favorites

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omtorney.snapcase.presentation.common.BottomBar
import com.omtorney.snapcase.presentation.common.CaseColumn

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.value

    Scaffold(bottomBar = { BottomBar(navController = navController) }) { paddingValues ->
        CaseColumn(
            items = state.cases,
            modifier = Modifier.padding(paddingValues)
        )
    }
}