package com.omtorney.snapcase.common.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.omtorney.snapcase.R

sealed class Screen(val route: String, @StringRes val title: Int, @DrawableRes val icon: Int?) {
    object Home : Screen("home_screen", R.string.search, R.drawable.ic_round_home)
    object Schedule : Screen("schedule_screen", R.string.schedule, null)
    object Search : Screen("search_screen", R.string.search, null)
    object Detail : Screen("detail_screen", R.string.detail, null)
    object Favorites : Screen("favorites_screen", R.string.favorites, R.drawable.ic_round_case)
    object Recent : Screen("recent_screen", R.string.recent, R.drawable.ic_round_history)
    object Act : Screen("act_screen", R.string.act, null)
    object Settings : Screen("settings_screen", R.string.settings, null)

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}