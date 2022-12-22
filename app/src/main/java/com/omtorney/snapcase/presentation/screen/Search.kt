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
import com.omtorney.snapcase.presentation.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    input: String,
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    viewModel.searchCase(input)
    val cases by viewModel.search.collectAsState(listOf())
    Scaffold(bottomBar = { BottomBar(navController = navController)}) { padding ->
        CaseColumn(
            cases,
            Modifier.padding(padding)
        )
    }
}