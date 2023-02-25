package com.omtorney.snapcase.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.omtorney.snapcase.presentation.act.ActScreen
import com.omtorney.snapcase.presentation.detail.DetailScreen
import com.omtorney.snapcase.presentation.favorites.FavoritesScreen
import com.omtorney.snapcase.presentation.home.HomeScreen
import com.omtorney.snapcase.presentation.recent.RecentScreen
import com.omtorney.snapcase.presentation.schedule.ScheduleScreen
import com.omtorney.snapcase.presentation.search.SearchScreen
import com.omtorney.snapcase.presentation.settings.SettingsScreen
import com.omtorney.snapcase.presentation.ui.theme.SnapCaseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnapCaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {

                        composable(route = Screen.Home.route) {
                            HomeScreen(
                                navController = navController,
                                onSearchClick = { caseType, searchInput ->
                                    navController.navigate(Screen.Search.route + "?caseType=$caseType&searchInput=$searchInput") {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onScheduleClick = { scheduleDate ->
                                    navController.navigate(Screen.Schedule.route + "/$scheduleDate") {
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
                            route = Screen.Search.route + "?caseType={caseType}&searchInput={searchInput}",
                            arguments = listOf(
                                navArgument(name = "caseType") { NavType.StringType },
                                navArgument(name = "searchInput") { NavType.StringType }
                            )
                        ) {
                            SearchScreen(
                                navController = navController,
                                onCardClick = { goToCaseDetail(it.number, Screen.Search, navController) },
                                onActTextClick = { goToActText(it, navController) }
                            )
                        }

                        composable(
                            route = Screen.Schedule.route + "/{scheduleDate}",
                            arguments = listOf(navArgument(name = "scheduleDate") { NavType.StringType })
                        ) {
                            ScheduleScreen(
                                navController = navController,
                                onCardClick = { goToCaseDetail(it.number, Screen.Schedule, navController) },
                                onActTextClick = { goToActText(it, navController) }
                            )
                        }

                        composable(
                            route = Screen.Detail.route + "/{caseNumber}",
                            arguments = listOf(navArgument(name = "caseNumber") { NavType.StringType })
                        ) {
                            DetailScreen(
                                onActTextClick = { goToActText(it, navController) },
                                onDismiss = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = Screen.Act.route + "/{caseActUrl}",
                            arguments = listOf(navArgument(name = "caseActUrl") { NavType.StringType })
                        ) {
                            ActScreen()
                        }

                        composable(route = Screen.Favorites.route) {
                            FavoritesScreen(
                                navController = navController,
                                onSettingsClick = { goToSettings(navController) },
                                onBackClick = { navController.popBackStack() },
                                onCardClick = { goToCaseDetail(it.number, Screen.Favorites, navController) },
                                onActTextClick = { goToActText(it, navController) }
                            )
                        }

                        composable(route = Screen.Recent.route) {
                            RecentScreen(
                                navController = navController,
                                onSettingsClick = { goToSettings(navController) },
                                onBackClick = { navController.popBackStack() },
                                onCardClick = { goToCaseDetail(it.number, Screen.Recent, navController) },
                                onActTextClick = { goToActText(it, navController) }
                            )
                        }

                        composable(route = Screen.Settings.route) {
                            SettingsScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
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
}