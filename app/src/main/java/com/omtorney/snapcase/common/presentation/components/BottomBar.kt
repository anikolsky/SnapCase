package com.omtorney.snapcase.common.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.omtorney.snapcase.common.presentation.Screen

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        Screen.Home,
        Screen.Favorites,
        Screen.Recent
    )
    BottomNavigation(
        backgroundColor = Color.DarkGray,
        contentColor = Color.White,
        modifier = Modifier.graphicsLayer {
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            clip = true
        }
    ) {
        val context = LocalContext.current
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon!!),
                        contentDescription = context.resources.getString(item.title)
                    )
                },
                label = { Text(text = context.resources.getString(item.title)) },
//                selectedContentColor = Color.White,
//                unselectedContentColor = Color.White.copy(0.6f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(route = item.route) {
                        popUpTo(route = navController.graph.findStartDestination().displayName) {
//                            saveState = true
                            inclusive = true
                        }
                        launchSingleTop = true
//                        restoreState = true
                    }
                }
            )
        }
    }
}