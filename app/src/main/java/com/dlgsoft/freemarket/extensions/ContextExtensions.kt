package com.dlgsoft.freemarket.extensions

import android.content.Context
import com.dlgsoft.freemarket.R
import com.dlgsoft.freemarket.utils.AppResult

fun Context.noNetworkConnectivityError(): AppResult.Error {
    return AppResult.Error(Exception(this.resources.getString(R.string.no_network_connectivity)))
}