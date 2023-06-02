package com.omtorney.snapcase.settings.presentation

enum class CheckPeriod(val period: Long, val title: String) {
    FIFTEEN_MINUTES(15L,"15 минут"),
    ONE_HOUR(60L, "1 час"),
    SIX_HOURS(180L, "3 часа")
}
