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
import com.omtorney.snapcase.presentation.viewmodel.ScheduleViewModel

@Composable
fun ScheduleScreen(
    date: String?,
    navController: NavController,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val cases by viewModel.schedule.collectAsState(listOf())
    viewModel.showSchedule(date!!)
    Scaffold(bottomBar = { BottomBar(navController = navController) }) { padding ->
        CaseColumn(cases, Modifier.padding(padding))
    }
}