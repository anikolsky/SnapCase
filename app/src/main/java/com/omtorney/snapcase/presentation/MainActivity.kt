package com.omtorney.snapcase.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
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
                                onSearchClick = { searchInput ->
                                    navController.navigate(Screen.Search.route + "/$searchInput") {
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
                                onSettingsClick = {
                                    navController.navigate(Screen.Settings.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }

                        composable(
                            route = Screen.Schedule.route + "/{scheduleDate}",
                            arguments = listOf(navArgument(name = "scheduleDate") { NavType.StringType })
                        ) {
                            ScheduleScreen(
                                navController = navController,
                                onCardClick = { caseNumber ->
                                    val caseNumberParam = caseNumber.replace("/", "+")
                                    navController.navigate(Screen.Detail.route + "/$caseNumberParam") {
                                        popUpTo(Screen.Schedule.route) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }

                        composable(
                            route = Screen.Search.route + "/{searchInput}",
                            arguments = listOf(navArgument(name = "searchInput") { NavType.StringType })
                        ) {
                            SearchScreen(
                                navController = navController,
                                onCardClick = { caseNumber ->
                                    val caseNumberParam = caseNumber.replace("/", "+")
                                    navController.navigate(Screen.Detail.route + "/$caseNumberParam") {
                                        popUpTo(Screen.Schedule.route) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }

                        composable(
                            route = Screen.Detail.route + "/{caseNumber}",
                            arguments = listOf(navArgument(name = "caseNumber") { NavType.StringType })
                        ) {
                            DetailScreen()
                        }

                        composable(
                            route = Screen.Act.route + "/{url}",
                            arguments = listOf(navArgument(name = "url") { NavType.StringType })
                        ) {
                            ActScreen()
                        }

                        composable(route = Screen.Favorites.route) {
                            FavoritesScreen(
                                navController = navController,
                                onSettingsClick = {
                                    navController.navigate(Screen.Settings.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(route = Screen.Recent.route) {
                            RecentScreen(
                                navController = navController,
                                onSettingsClick = {
                                    navController.navigate(Screen.Settings.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onBackClick = { navController.popBackStack() }
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
}