package com.omtorney.snapcase.presentation

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object ScheduleScreen : Screen("schedule_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}