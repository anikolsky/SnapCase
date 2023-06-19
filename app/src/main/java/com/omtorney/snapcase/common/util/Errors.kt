package com.omtorney.snapcase.common.util

import java.io.IOException
import java.net.SocketTimeoutException

sealed class CustomError(message: String) : Exception(message) {
    class NoResultFound : CustomError("Результаты по запросу не найдены")
    class NoScheduledCases : CustomError("На выбранную дату дел не назначено")
    class SiteDataUnavailable : CustomError("Информация в данный момент недоступна по причине ошибки на сайте")
}

fun handleException(e: Exception): String {
    return when (e) {
        is CustomError -> e.message!!
        is SocketTimeoutException -> "Сайт недоступен\n\n(Socket timeout exception: ${e.localizedMessage})"
        is IOException -> "IOException: ${e.localizedMessage}"
//        is HttpException -> "Http error ${e.code()}"
        else -> "Unexpected error: ${e.localizedMessage}"
    }
}
