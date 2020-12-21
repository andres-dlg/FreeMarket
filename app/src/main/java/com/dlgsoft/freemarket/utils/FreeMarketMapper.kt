package com.dlgsoft.freemarket.utils

class FreeMarketMapper {
    private val currencyMap = hashMapOf(
        Pair("ARS", "$"),
        Pair("BOB", "Bs."),
        Pair("BRL", "R$"),
        Pair("CLP", "$"),
        Pair("COP", "$"),
        Pair("CRC", "₡"),
        Pair("CUP", "$"),
        Pair("DOP", "$"),
        Pair("GTQ", "Q"),
        Pair("HNL", "L"),
        Pair("MXN", "$"),
        Pair("NIO", "C$"),
        Pair("PAB", "B/."),
        Pair("PYG", "₲"),
        Pair("PEN", "S/"),
        Pair("UYU", "$"),
        Pair("VES", "Bs"),
        Pair("USD", "U\$D")
    )

    private val conditionMap = hashMapOf(
        Pair("new", "Nuevo"),
        Pair("used", "Usado")
    )

    fun getCurrencySymbol(code: String): String {
        return currencyMap[code] ?: ""
    }

    fun getCondition(condition: String): String {
        return conditionMap[condition] ?: ""
    }
}