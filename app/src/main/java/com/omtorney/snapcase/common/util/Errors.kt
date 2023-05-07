package com.omtorney.snapcase.common.util

import java.net.SocketTimeoutException

class NoResultFound : Throwable()
class NoScheduledCases : Throwable()
class SiteDataUnavailable : Throwable()

fun handleException(e: Throwable): String {
    return when (e) {
        is NoResultFound -> "Результаты по запросу не найдены"
        is NoScheduledCases -> "На выбранную дату дел не назначено"
        is SiteDataUnavailable -> "Информация в данный момент недоступна по причине ошибки на сайте"
//        is HttpException -> "Http error ${e.code()}"
//        is IOException -> ""
        is SocketTimeoutException -> "Socket timeout error"
        else -> "Unexpected error: ${e.localizedMessage}"
    }
}