package com.omtorney.snapcase.common.presentation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.omtorney.snapcase.act.presentation.ActScreen
import com.omtorney.snapcase.act.presentation.ActViewModel
import com.omtorney.snapcase.common.util.Constants.DEEPLINK_URI
import com.omtorney.snapcase.detail.presentation.DetailNotificationScreen
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
    navController: NavHostController,
    onAppSettingsClick: () -> Unit
) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val accentColor by mainViewModel.accentColor.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(route = Screen.Home.route) { backStackEntry ->
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
                onSettingsClick = { navigateToSettings(backStackEntry, navController) }
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
        ) { backStackEntry ->
            val viewModel: SearchViewModel = hiltViewModel()
            val state = viewModel.state.value
            SearchScreen(
                state = state,
                accentColor = Color(accentColor),
                onCardClick = { case ->
                    navigateToDetail(
                        url = case.url,
                        number = case.number,
                        hearingDateTime = "",
                        actDateForce = case.actDateForce,
                        actTextUrl = case.actTextUrl,
                        courtTitle = case.courtTitle,
                        currentScreen = backStackEntry,
                        navController = navController
                    )
                },
                onActTextClick = { case ->
                    navigateToActText(case.courtTitle, case.actTextUrl, backStackEntry, navController)
                }
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
        ) { backStackEntry ->
            val viewModel: ScheduleViewModel = hiltViewModel()
            val state = viewModel.state.value
            ScheduleScreen(
                state = state,
                onEvent = viewModel::onEvent,
                accentColor = Color(accentColor),
                onActTextClick = { case ->
                    navigateToActText(case.courtTitle, case.actTextUrl, backStackEntry, navController)
                },
                onCardClick = { case ->
                    navigateToDetail(
                        url = case.url,
                        number = case.number,
                        hearingDateTime = case.hearingDateTime,
                        actDateForce = "",
                        actTextUrl = case.actTextUrl,
                        courtTitle = case.courtTitle,
                        currentScreen = backStackEntry,
                        navController = navController
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
        ) { backStackEntry ->
            val viewModel: DetailViewModel = hiltViewModel()
            val state = viewModel.state.value
            DetailScreen(
                accentColor = Color(accentColor),
                state = state,
                onEvent = viewModel::onEvent,
                onActTextClick = { case ->
                    navigateToActText(case.courtTitle, case.actTextUrl, backStackEntry, navController)
                },
                onDismiss = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.DetailNotification.route,
            arguments = listOf(
                navArgument("courtTitle") { type = NavType.StringType },
                navArgument("number") { type = NavType.StringType },
                navArgument("event") { type = NavType.StringType },
                navArgument("participants") { type = NavType.StringType },
                navArgument("url") { type = NavType.StringType },
                navArgument("hearingDateTime") { type = NavType.StringType },
                navArgument("actDateForce") { type = NavType.StringType },
                navArgument("actTextUrl") { type = NavType.StringType }
            ),
            deepLinks = listOf(navDeepLink { uriPattern = DEEPLINK_URI +
                    "?courtTitle={courtTitle}" +
                    "&number={number}" +
                    "&event={event}" +
                    "&participants={participants}" +
                    "&url={url}" +
                    "&hearingDateTime={hearingDateTime}" +
                    "&actDateForce={actDateForce}" +
                    "&actTextUrl={actTextUrl}"
                }
            )
        ) { backStackEntry ->
            val courtTitle = backStackEntry.arguments?.getString("courtTitle") ?: ""
            val number = backStackEntry.arguments?.getString("number") ?: ""
            val event = backStackEntry.arguments?.getString("event") ?: ""
            val participants = backStackEntry.arguments?.getString("participants") ?: ""
            val url = backStackEntry.arguments?.getString("url") ?: ""
            val hearingDateTime = backStackEntry.arguments?.getString("hearingDateTime") ?: ""
            val actDateForce = backStackEntry.arguments?.getString("actDateForce") ?: ""
            val actTextUrl = backStackEntry.arguments?.getString("actTextUrl") ?: ""

            DetailNotificationScreen(
                accentColor = Color(accentColor),
                courtTitle = courtTitle,
                number = number,
                event = event,
                participants = participants,
                onClick = {
                    navigateToDetail(
                        url = url,
                        number = number,
                        hearingDateTime = hearingDateTime,
                        actDateForce = actDateForce,
                        actTextUrl = actTextUrl,
                        courtTitle = courtTitle,
                        currentScreen = backStackEntry,
                        navController = navController
                    )
                }
            )
        }

        composable(
            route = Screen.Act.route +
                    "?courtTitle={courtTitle}" +
                    "&url={url}",
            arguments = listOf(
                navArgument(name = "courtTitle") { NavType.StringType },
                navArgument(name = "url") { NavType.StringType }
            )
        ) {
            val viewModel: ActViewModel = hiltViewModel()
            val state = viewModel.state.value
            ActScreen(
                state = state,
                accentColor = Color(accentColor)
            )
        }

        composable(route = Screen.Favorites.route) { backStackEntry ->
            val viewModel: FavoritesViewModel = hiltViewModel()
            val state = viewModel.state.value
            FavoritesScreen(
                navController = navController,
                state = state,
//                onEvent = viewModel::onEvent,
                accentColor = Color(accentColor),
                onSettingsClick = { navigateToSettings(backStackEntry, navController) },
//                onBackClick = { navController.popBackStack() },
                onActTextClick = { case ->
                    navigateToActText(case.courtTitle, case.actTextUrl, backStackEntry, navController)
                },
                onCardClick = { case ->
                    navigateToDetail(
                        url = case.url,
                        number = case.number,
                        hearingDateTime = case.hearingDateTime,
                        actDateForce = case.actDateForce,
                        actTextUrl = case.actTextUrl,
                        courtTitle = case.courtTitle,
                        currentScreen = backStackEntry,
                        navController = navController
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
                onAppSettingsClick = onAppSettingsClick
            )
        }
    }
}

private fun navigateToDetail(
    url: String,
    number: String,
    hearingDateTime: String,
    actDateForce: String,
    actTextUrl: String,
    courtTitle: String,
    currentScreen: NavBackStackEntry,
    navController: NavController
) {
    val destinationRoute = currentScreen.destination.route!!

    navController.navigate(
        Screen.Detail.route +
                "?url=${Uri.encode(url)}" +
                "&number=${Uri.encode(number)}" +
                "&hearingDateTime=$hearingDateTime" +
                "&actDateForce=$actDateForce" +
                "&actTextUrl=${Uri.encode(actTextUrl)}" +
                "&courtTitle=$courtTitle"
    ) {
        popUpTo(destinationRoute) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun navigateToActText(
    courtTitle: String,
    url: String,
    currentScreen: NavBackStackEntry,
    navController: NavController
) {
    val destinationRoute = currentScreen.destination.route!!

    navController.navigate(Screen.Act.route +
            "?courtTitle=${courtTitle}" +
            "&url=${Uri.encode(url)}"
    ) {
        popUpTo(destinationRoute) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun navigateToSettings(
    currentScreen: NavBackStackEntry,
    navController: NavController
) {
    val destinationRoute = currentScreen.destination.route!!

    navController.navigate(Screen.Settings.route) {
        popUpTo(destinationRoute) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
