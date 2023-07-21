package com.omtorney.snapcase.presentation.screen.settings

enum class CheckPeriod(
    val period: Long,
    val title: String
) {
    FIFTEEN_MINUTES(15L,"15 мин."),
    ONE_HOUR(60L, "1 час"),
    THREE_HOURS(180L, "3 часа"),
    SIX_HOURS(360L, "6 часов"),
    TWELVE_HOURS(720, "12 часов")
}
