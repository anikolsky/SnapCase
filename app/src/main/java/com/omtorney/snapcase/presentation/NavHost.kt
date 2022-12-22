package com.omtorney.snapcase.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.presentation.screen.*

@Composable
fun AppNavHost(navController: NavController) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = NavigationItem.Home.route
    ) {
        composable(route = NavigationItem.Home.route) { MainScreen(navController) }
        composable(route = NavigationItem.Favorites.route) { FavoritesScreen(navController) }
        composable(route = NavigationItem.News.route) { NewsScreen(navController) }
        composable(
            route = "schedule/{date}",
            arguments = listOf(navArgument("date") { NavType.StringType })
        ) { ScheduleScreen(date = it.arguments?.getString("date") ?: "", navController) }
        composable(
            route = "search/{input}",
            arguments = listOf(navArgument("input") { NavType.StringType })
        ) { SearchScreen(input = it.arguments?.getString("input") ?: "", navController) }
        composable(route = "details") { DetailScreen(caseParam = Case()) }
        composable(
            route = "act/{url}",
            arguments = listOf(navArgument("url") { NavType.StringType })
            ) { ActTextScreen(url = it.arguments?.getString("url")) }
    }
}