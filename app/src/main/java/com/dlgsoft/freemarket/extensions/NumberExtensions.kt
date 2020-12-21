package com.dlgsoft.freemarket.extensions

import kotlin.math.roundToInt

fun Double.roundIfNotDecimal(): Number {
    return if (this.rem(1).equals(0.0)) {
        this.roundToInt()
    } else {
        this
    }
}