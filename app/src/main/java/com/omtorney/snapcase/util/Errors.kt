package com.omtorney.snapcase.util

import java.net.SocketTimeoutException

class NoResultFound : Throwable()
class SiteDataUnavailable : Throwable()

fun handleException(e: Throwable): String {
    return when (e) {
        is NoResultFound -> "Результаты по запросу не найдены"
        is SiteDataUnavailable -> "Информация в данный момент недоступна по причине ошибки на сайте"
//        is HttpException -> "Http error ${e.code()}"
        is SocketTimeoutException -> "Socket timeout error"
        else -> "Unexpected error: ${e.localizedMessage}"
    }
}