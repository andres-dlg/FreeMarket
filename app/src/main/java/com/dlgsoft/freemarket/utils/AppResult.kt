package com.dlgsoft.freemarket.utils

import retrofit2.Response

sealed class AppResult<out T> {

    data class Success<out T>(val successData: T) : AppResult<T>()

    class Error(
        val exception: java.lang.Exception,
        val message: String = exception.localizedMessage ?: ""
    ) : AppResult<Nothing>()
}

fun <T : Any> handleApiError(resp: Response<T>): AppResult.Error {
    val error = ApiErrorUtils.parseError(resp)
    return AppResult.Error(Exception(error.message))
}