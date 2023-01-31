package com.omtorney.snapcase.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.omtorney.snapcase.presentation.act.ActScreen
import com.omtorney.snapcase.presentation.detail.DetailScreen
import com.omtorney.snapcase.presentation.favorites.FavoritesScreen
import com.omtorney.snapcase.presentation.home.HomeScreen
import com.omtorney.snapcase.presentation.schedule.ScheduleScreen
import com.omtorney.snapcase.presentation.search.SearchScreen
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
                            HomeScreen(navController = navController)
                        }
                        composable(route = Screen.Favorites.route) {
                            FavoritesScreen(navController = navController)
                        }
                        composable(
                            route = Screen.Schedule.route + "/{date}",
                            arguments = listOf(navArgument("date") { NavType.StringType })
                        ) {
                            ScheduleScreen(
                                date = it.arguments?.getString("date") ?: "",
                                navController = navController
                            )
                        }
                        composable(
                            route = Screen.Search.route + "/{input}",
                            arguments = listOf(navArgument("input") { NavType.StringType })
                        ) {
                            SearchScreen(
                                input = it.arguments?.getString("input") ?: "",
                                navController = navController
                            )
                        }
                        composable(route = Screen.Detail.route) {
                            DetailScreen(caseParam = null)
                        }
                        composable(
                            route = Screen.Act.route + "/{url}",
                            arguments = listOf(navArgument("url") { NavType.StringType })
                        ) {
                            ActScreen(url = it.arguments?.getString("url"))
                        }
                    }
                }
            }
        }
    }
}