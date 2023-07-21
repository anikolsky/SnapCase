package com.omtorney.snapcase.presentation

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
import com.omtorney.snapcase.presentation.screen.act.ActScreen
import com.omtorney.snapcase.presentation.screen.act.ActViewModel
import com.omtorney.snapcase.util.Constants.DEEPLINK_URI
import com.omtorney.snapcase.presentation.screen.detail.DetailNotificationScreen
import com.omtorney.snapcase.presentation.screen.detail.DetailScreen
import com.omtorney.snapcase.presentation.screen.detail.DetailViewModel
import com.omtorney.snapcase.presentation.screen.favorites.FavoritesScreen
import com.omtorney.snapcase.presentation.screen.favorites.FavoritesViewModel
import com.omtorney.snapcase.firebase.auth.SignInState
import com.omtorney.snapcase.firebase.auth.UserData
import com.omtorney.snapcase.presentation.screen.home.HomeScreen
import com.omtorney.snapcase.presentation.screen.home.HomeViewModel
import com.omtorney.snapcase.presentation.screen.schedule.ScheduleScreen
import com.omtorney.snapcase.presentation.screen.schedule.ScheduleViewModel
import com.omtorney.snapcase.presentation.screen.search.SearchScreen
import com.omtorney.snapcase.presentation.screen.search.SearchViewModel
import com.omtorney.snapcase.presentation.screen.settings.SettingsScreen
import com.omtorney.snapcase.presentation.screen.settings.SettingsViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    accentColor: Long,
    signInState: SignInState,
    userData: UserData?,
    onAppSettingsClick: () -> Unit,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onResetState: () -> Unit
) {
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
                        number = case.number,
                        uid = case.uid,
                        url = case.url,
                        permanentUrl = case.permanentUrl,
                        courtTitle = case.courtTitle,
                        hearingDateTime = "",
                        actDateForce = case.actDateForce,
                        actTextUrl = case.actTextUrl,
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
                        number = case.number,
                        uid = case.uid,
                        url = case.url,
                        permanentUrl = case.permanentUrl,
                        courtTitle = case.courtTitle,
                        hearingDateTime = case.hearingDateTime,
                        actDateForce = "",
                        actTextUrl = case.actTextUrl,
                        currentScreen = backStackEntry,
                        navController = navController
                    )
                }
            )
        }

        composable(
            route = Screen.Detail.route +
                    "?number={number}" +
                    "&uid={uid}" +
                    "&url={url}" +
                    "&permanentUrl={permanentUrl}" +
                    "&courtTitle={courtTitle}" +
                    "&hearingDateTime={hearingDateTime}" +
                    "&actDateForce={actDateForce}" +
                    "&actTextUrl={actTextUrl}",
            arguments = listOf(
                navArgument(name = "number") { NavType.StringType },
                navArgument(name = "uid") { NavType.StringType },
                navArgument(name = "url") { NavType.StringType },
                navArgument(name = "permanentUrl") { NavType.StringType },
                navArgument(name = "courtTitle") { NavType.StringType },
                navArgument(name = "hearingDateTime") { NavType.StringType },
                navArgument(name = "actDateForce") { NavType.StringType },
                navArgument(name = "actTextUrl") { NavType.StringType }
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
                navArgument("event") { type = NavType.StringType },
                navArgument("number") { type = NavType.StringType },
                navArgument("uid") { type = NavType.StringType },
                navArgument("url") { type = NavType.StringType },
                navArgument("permanentUrl") { type = NavType.StringType },
                navArgument("courtTitle") { type = NavType.StringType },
                navArgument("participants") { type = NavType.StringType },
                navArgument("hearingDateTime") { type = NavType.StringType },
                navArgument("actDateForce") { type = NavType.StringType },
                navArgument("actTextUrl") { type = NavType.StringType }
            ),
            deepLinks = listOf(navDeepLink { uriPattern = DEEPLINK_URI +
                    "?event={event}" +
                    "&number={number}" +
                    "&uid={uid}" +
                    "&url={url}" +
                    "&permanentUrl={permanentUrl}" +
                    "&courtTitle={courtTitle}" +
                    "&participants={participants}" +
                    "&hearingDateTime={hearingDateTime}" +
                    "&actDateForce={actDateForce}" +
                    "&actTextUrl={actTextUrl}"
                }
            )
        ) { backStackEntry ->
            fun getArg(arg: String): String = backStackEntry.arguments?.getString(arg) ?: ""

            val event = getArg("event")
            val number = getArg("number")
            val uid = getArg("uid")
            val url = getArg("url")
            val permanentUrl = getArg("permanentUrl")
            val courtTitle = getArg("courtTitle")
            val participants = getArg("participants")
            val hearingDateTime = getArg("hearingDateTime")
            val actDateForce = getArg("actDateForce")
            val actTextUrl = getArg("actTextUrl")
            DetailNotificationScreen(
                accentColor = Color(accentColor),
                courtTitle = courtTitle,
                number = number,
                event = event,
                participants = participants,
                onClick = {
                    navigateToDetail(
                        number = number,
                        uid = uid,
                        url = url,
                        permanentUrl = permanentUrl,
                        courtTitle = courtTitle,
                        hearingDateTime = hearingDateTime,
                        actDateForce = actDateForce,
                        actTextUrl = actTextUrl,
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
                        number = case.number,
                        uid = case.uid,
                        url = case.url,
                        permanentUrl = case.permanentUrl,
                        courtTitle = case.courtTitle,
                        hearingDateTime = case.hearingDateTime,
                        actDateForce = case.actDateForce,
                        actTextUrl = case.actTextUrl,
                        currentScreen = backStackEntry,
                        navController = navController
                    )
                }
            )
        }

        composable(route = Screen.Settings.route) {
            val viewModel: SettingsViewModel = hiltViewModel()
            val state = viewModel.settingsState.value
            val workInfo = viewModel.workInfo.observeAsState().value
            val firestoreUserState = viewModel.firestoreUserState.collectAsStateWithLifecycle().value
            SettingsScreen(
                settingsState = state,
                accentColor = Color(accentColor),
                userData = userData,
                signInState = signInState,
                firestoreUserState = firestoreUserState,
                workInfo = workInfo,
                onEvent = viewModel::onEvent,
                onBackClick = { navController.popBackStack() },
                onAppSettingsClick = onAppSettingsClick,
                onSignInClick = onSignInClick,
                onSignOutClick = onSignOutClick,
                onResetState = onResetState
            )
        }
    }
}

private fun navigateToDetail(
    number: String,
    uid: String,
    url: String,
    permanentUrl: String,
    courtTitle: String,
    hearingDateTime: String,
    actDateForce: String,
    actTextUrl: String,
    currentScreen: NavBackStackEntry,
    navController: NavController
) {
    val destinationRoute = currentScreen.destination.route!!

    fun enc(arg: String) = Uri.encode(arg)

    navController.navigate(
        Screen.Detail.route +
                "?number=${enc(number)}" +
                "&uid=${enc(uid)}" +
                "&url=${enc(url)}" +
                "&permanentUrl=${enc(permanentUrl)}" +
                "&courtTitle=${enc(courtTitle)}" +
                "&hearingDateTime=${enc(hearingDateTime)}" +
                "&actDateForce=${enc(actDateForce)}" +
                "&actTextUrl=${enc(actTextUrl)}"
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

    navController.navigate(
        Screen.Act.route +
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
