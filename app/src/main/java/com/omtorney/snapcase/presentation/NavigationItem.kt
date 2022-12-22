package com.omtorney.snapcase.presentation

import com.omtorney.snapcase.R

sealed class NavigationItem(
    var route: String,
    var icon: Int,
    var title: String
) {
    object Home : NavigationItem("home", R.drawable.ic_round_home, "Поиск")
    object Favorites : NavigationItem("favorites", R.drawable.ic_round_case, "Избранное")
    object News : NavigationItem("news", R.drawable.ic_round_newspaper, "Новости")
}
