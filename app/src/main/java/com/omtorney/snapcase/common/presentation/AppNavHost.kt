package com.omtorney.snapcase.common.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.omtorney.snapcase.act.presentation.ActScreen
import com.omtorney.snapcase.act.presentation.ActViewModel
import com.omtorney.snapcase.detail.presentation.DetailScreen
import com.omtorney.snapcase.detail.presentation.DetailViewModel
import com.omtorney.snapcase.favorites.presentation.FavoritesScreen
import com.omtorney.snapcase.favorites.presentation.FavoritesViewModel
import com.omtorney.snapcase.home.presentation.HomeScreen
import com.omtorney.snapcase.home.presentation.HomeViewModel
import com.omtorney.snapcase.recent.presentation.RecentScreen
import com.omtorney.snapcase.recent.presentation.RecentViewModel
import com.omtorney.snapcase.schedule.presentation.ScheduleScreen
import com.omtorney.snapcase.schedule.presentation.ScheduleViewModel
import com.omtorney.snapcase.search.presentation.SearchScreen
import com.omtorney.snapcase.search.presentation.SearchViewModel
import com.omtorney.snapcase.settings.presentation.SettingsScreen
import com.omtorney.snapcase.settings.presentation.SettingsViewModel

@Composable
fun AppNavHost(
    onGoToAppSettingsClick: () -> Unit
) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()

    val accentColor by mainViewModel.accentColor.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(route = Screen.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            val state by viewModel.state
            HomeScreen(
                navController = navController,
                state = state,
                onEvent = viewModel::onEvent,
                accentColor = Color(accentColor),
                onSearchClick = { caseType, courtTitle, searchInput ->
                    navController.navigate(Screen.Search.route + "?caseType=$caseType&courtTitle=$courtTitle&searchInput=$searchInput") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onScheduleClick = { scheduleDate, courtTitle ->
                    navController.navigate(Screen.Schedule.route + "?scheduleDate=$scheduleDate&courtTitle=$courtTitle") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSettingsClick = { goToSettings(navController) }
            )
        }

        composable(
            route = Screen.Search.route + "?caseType={caseType}&courtTitle={courtTitle}&searchInput={searchInput}",
            arguments = listOf(
                navArgument(name = "caseType") { NavType.StringType },
                navArgument(name = "courtTitle") { NavType.StringType },
                navArgument(name = "searchInput") { NavType.StringType }
            )
        ) {
            val viewModel: SearchViewModel = hiltViewModel()
            val state = viewModel.state.value
            SearchScreen(
                navController = navController,
                state = state,
                onEvent = viewModel::onEvent,
                accentColor = Color(accentColor),
                onCardClick = { goToCaseDetail(it.number, Screen.Search, navController) },
                onActTextClick = { goToActText(it, navController) }
            )
        }

        composable(
            route = Screen.Schedule.route + "?scheduleDate={scheduleDate}&courtTitle={courtTitle}",
            arguments = listOf(
                navArgument(name = "scheduleDate") { NavType.StringType },
                navArgument(name = "courtTitle") { NavType.StringType }
            )
        ) {
            val viewModel: ScheduleViewModel = hiltViewModel()
            val state = viewModel.state.value
            ScheduleScreen(
                navController = navController,
                state = state,
                onEvent = viewModel::onEvent,
                accentColor = Color(accentColor),
                onCardClick = { goToCaseDetail(it.number, Screen.Schedule, navController) },
                onActTextClick = { goToActText(it, navController) }
            )
        }

        composable(
            route = Screen.Detail.route + "/{caseNumber}",
            arguments = listOf(navArgument(name = "caseNumber") { NavType.StringType })
        ) {
            val viewModel: DetailViewModel = hiltViewModel()
            val state = viewModel.state.value
            DetailScreen(
                accentColor = Color(accentColor),
                state = state,
                onEvent = viewModel::onEvent,
                onActTextClick = { goToActText(it, navController) },
                onDismiss = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Act.route + "/{caseActUrl}",
            arguments = listOf(navArgument(name = "caseActUrl") { NavType.StringType })
        ) {
            val viewModel: ActViewModel = hiltViewModel()
            val state = viewModel.state.value
            ActScreen(
                state = state,
                accentColor = Color(accentColor)
            )
        }

        composable(route = Screen.Favorites.route) {
            val viewModel: FavoritesViewModel = hiltViewModel()
            val state = viewModel.state.value
            FavoritesScreen(
                navController = navController,
                state = state,
                onEvent = viewModel::onEvent,
                accentColor = Color(accentColor),
                onSettingsClick = { goToSettings(navController) },
                onBackClick = { navController.popBackStack() },
                onCardClick = { goToCaseDetail(it.number, Screen.Favorites, navController) },
                onActTextClick = { goToActText(it, navController) }
            )
        }

        composable(route = Screen.Recent.route) {
            val viewModel: RecentViewModel = hiltViewModel()
            val state = viewModel.state.value
            RecentScreen(
                navController = navController,
                state = state,
                onEvent = viewModel::onEvent,
                accentColor = Color(accentColor),
                onSettingsClick = { goToSettings(navController) },
                onBackClick = { navController.popBackStack() },
                onCardClick = { goToCaseDetail(it.number, Screen.Recent, navController) },
                onActTextClick = { goToActText(it, navController) }
            )
        }

        composable(route = Screen.Settings.route) {
            val viewModel: SettingsViewModel = hiltViewModel()
            val state = viewModel.state
            SettingsScreen(
                state = state,
                onEvent = viewModel::onEvent,
                accentColor = Color(accentColor),
                onBackClick = { navController.popBackStack() },
                onGoToAppSettingsClick = onGoToAppSettingsClick
            )
        }
    }
}

private fun goToCaseDetail(number: String, screen: Screen, navController: NavController) {
    val caseNumberParam = number.replace("/", "+")
    navController.navigate(Screen.Detail.route + "/$caseNumberParam") {
        popUpTo(
            when (screen) {
                Screen.Search -> Screen.Search.route
                Screen.Schedule -> Screen.Schedule.route
                Screen.Favorites -> Screen.Favorites.route
                Screen.Recent -> Screen.Recent.route
                else -> ""
            }
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun goToActText(url: String, navController: NavController) {
    val caseActUrlParam = url.replace("/", "+").replace("?", "!")
    navController.navigate(Screen.Act.route + "/$caseActUrlParam") {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun goToSettings(navController: NavController) {
    navController.navigate(Screen.Settings.route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
