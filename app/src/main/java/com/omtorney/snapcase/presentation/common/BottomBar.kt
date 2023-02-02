package com.omtorney.snapcase.presentation.common

import android.content.Context
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.omtorney.snapcase.presentation.Screen

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        Screen.Home,
        Screen.Favorites,
        Screen.Recent
    )

    BottomNavigation(
        elevation = 12.dp,
        backgroundColor = MaterialTheme.colors.primary,
//        contentColor = contentColorFor(backgroundColor = MaterialTheme.colors.primary),
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
                        contentDescription = context.resources.getString(item.title!!)
                    )
                },
                label = { Text(text = context.resources.getString(item.title!!)) },
//                selectedContentColor = Color.White,
//                unselectedContentColor = Color.White.copy(0.6f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(route = item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route = route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}