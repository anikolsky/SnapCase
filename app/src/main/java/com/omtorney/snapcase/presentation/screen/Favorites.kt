package com.omtorney.snapcase.presentation.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omtorney.snapcase.presentation.BottomBar
import com.omtorney.snapcase.presentation.CaseColumn
import com.omtorney.snapcase.presentation.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val favorites by viewModel.allCases.collectAsState(listOf())
    Scaffold(bottomBar = { BottomBar(navController = navController)}) { padding ->
        CaseColumn(favorites, Modifier.padding(padding))
    }
}