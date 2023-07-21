package com.omtorney.snapcase.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.omtorney.snapcase.presentation.Screen
import com.omtorney.snapcase.presentation.theme.SnapCaseTheme

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        Screen.Home,
        Screen.Favorites,
//        Screen.Recent
    )
    NavigationBar(
//        containerColor = Color.DarkGray,
//        contentColor = Color.White,
//        modifier = Modifier.graphicsLayer {
//            shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
//            clip = true
//        }
    ) {
        val context = LocalContext.current
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon!!),
                        contentDescription = context.resources.getString(item.title)
                    )
                },
                label = { Text(text = context.resources.getString(item.title)) },
//                colors = NavigationBarItemDefaults.colors(),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(route = item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                            inclusive = true
                        }
                        launchSingleTop = true
//                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    SnapCaseTheme {
        Surface {
            BottomBar(navController = rememberNavController())
        }
    }
}