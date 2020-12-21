package com.dlgsoft.freemarket.extensions

/**
 * Reemplaza una coma por un punto.
 */
fun String.normalizeNumber(): String {
    return this.replaceFirst(",", ".")
}

/**
 * Chequea si un String es un numero.
 */
fun String.isNumeric(): Boolean {
    var isNumeric = true
    try {
        this.normalizeNumber().toFloat()
    } catch (e: NumberFormatException) {
        isNumeric = false
    }
    return isNumeric
}