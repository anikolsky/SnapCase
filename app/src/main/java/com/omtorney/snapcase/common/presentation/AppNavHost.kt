package com.omtorney.snapcase.common.presentation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
                    navController.navigate(
                        Screen.Search.route +
                                "?caseType=$caseType" +
                                "&courtTitle=$courtTitle" +
                                "&searchInput=$searchInput"
                    ) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onScheduleClick = { scheduleDate, courtTitle ->
                    navController.navigate(
                        Screen.Schedule.route +
                                "?scheduleDate=$scheduleDate" +
                                "&courtTitle=$courtTitle"
                    ) {
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
            route = Screen.Search.route +
                    "?caseType={caseType}" +
                    "&courtTitle={courtTitle}" +
                    "&searchInput={searchInput}",
            arguments = listOf(
                navArgument(name = "caseType") { NavType.StringType },
                navArgument(name = "courtTitle") { NavType.StringType },
                navArgument(name = "searchInput") { NavType.StringType }
            )
        ) {
            val viewModel: SearchViewModel = hiltViewModel()
            val state = viewModel.state.value
            SearchScreen(
                state = state,
                accentColor = Color(accentColor),
                onCardClick = { case ->
                    goToCaseDetail(
                        url = case.url,
                        number = case.number,
                        hearingDateTime = "",
                        actDateForce = case.actDateForce,
                        actTextUrl = case.actTextUrl,
                        courtTitle = case.courtTitle,
                        screen = Screen.Search,
                        navController = navController,
                    )
                },
                onActTextClick = { goToActText(it, navController) }
            )
        }

        composable(
            route = Screen.Schedule.route +
                    "?scheduleDate={scheduleDate}" +
                    "&courtTitle={courtTitle}",
            arguments = listOf(
                navArgument(name = "scheduleDate") { NavType.StringType },
                navArgument(name = "courtTitle") { NavType.StringType }
            )
        ) {
            val viewModel: ScheduleViewModel = hiltViewModel()
            val state = viewModel.state.value
            ScheduleScreen(
                state = state,
                onEvent = viewModel::onEvent,
                accentColor = Color(accentColor),
                onActTextClick = { goToActText(it, navController) },
                onCardClick = { case ->
                    goToCaseDetail(
                        url = case.url,
                        number = case.number,
                        hearingDateTime = case.hearingDateTime,
                        actDateForce = "",
                        actTextUrl = case.actTextUrl,
                        courtTitle = case.courtTitle,
                        screen = Screen.Schedule,
                        navController = navController,
                    )
                }
            )
        }

        composable(
            route = Screen.Detail.route +
                    "?url={url}" +
                    "&number={number}" +
                    "&hearingDateTime={hearingDateTime}" +
                    "&actDateForce={actDateForce}" +
                    "&actTextUrl={actTextUrl}" +
                    "&courtTitle={courtTitle}",
            arguments = listOf(
                navArgument(name = "url") { NavType.StringType },
                navArgument(name = "number") { NavType.StringType },
                navArgument(name = "hearingDateTime") { NavType.StringType },
                navArgument(name = "actDateForce") { NavType.StringType },
                navArgument(name = "actTextUrl") { NavType.StringType },
                navArgument(name = "courtTitle") { NavType.StringType }
            )
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
            route = Screen.Act.route + "?url={url}",
            arguments = listOf(navArgument(name = "url") { NavType.StringType })
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
//                onEvent = viewModel::onEvent,
                accentColor = Color(accentColor),
                onSettingsClick = { goToSettings(navController) },
//                onBackClick = { navController.popBackStack() },
                onActTextClick = { goToActText(it, navController) },
                onCardClick = { case ->
                    goToCaseDetail(
                        url = case.url,
                        number = case.number,
                        hearingDateTime = case.hearingDateTime,
                        actDateForce = case.actDateForce,
                        actTextUrl = case.actTextUrl,
                        courtTitle = case.courtTitle,
                        screen = Screen.Favorites,
                        navController = navController,
                    )
                }
            )
        }

        composable(route = Screen.Settings.route) {
            val viewModel: SettingsViewModel = hiltViewModel()
            val state = viewModel.state.value
            val workInfo = viewModel.workInfo.observeAsState().value
            SettingsScreen(
                state = state,
                workInfo = workInfo,
                onEvent = viewModel::onEvent,
                accentColor = Color(accentColor),
                onBackClick = { navController.popBackStack() },
                onGoToAppSettingsClick = onGoToAppSettingsClick
            )
        }
    }
}

private fun goToCaseDetail(
    url: String,
    number: String,
    hearingDateTime: String,
    actDateForce: String,
    actTextUrl: String,
    courtTitle: String,
    screen: Screen,
    navController: NavController
) {
    navController.navigate(
        Screen.Detail.route +
                "?url=${Uri.encode(url)}" +
                "&number=${Uri.encode(number)}" +
                "&hearingDateTime=$hearingDateTime" +
                "&actDateForce=$actDateForce" +
                "&actTextUrl=${Uri.encode(actTextUrl)}" +
                "&courtTitle=$courtTitle"
    ) {
        popUpTo(screen.route) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun goToActText(
    url: String,
    navController: NavController
) {
    navController.navigate(Screen.Act.route + "?url=${Uri.encode(url)}") {
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
