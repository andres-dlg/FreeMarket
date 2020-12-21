package com.dlgsoft.freemarket.utils

import android.util.Log
import com.google.gson.GsonBuilder
import retrofit2.Response
import java.io.IOException

data class APIError(val message: String) {
    constructor() : this("")
}

object ApiErrorUtils {

    private const val TAG: String = "API ERROR"

    fun parseError(response: Response<*>): APIError {

        val gson = GsonBuilder().create()
        val error: APIError

        try {
            error = gson.fromJson(response.errorBody()?.string(), APIError::class.java)
        } catch (e: IOException) {
            e.message?.let { Log.d(TAG, it) }
            return APIError()
        }
        return error
    }
}