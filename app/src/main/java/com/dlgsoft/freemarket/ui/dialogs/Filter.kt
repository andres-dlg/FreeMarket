package com.dlgsoft.freemarket.ui.dialogs

data class Filter(
    val minPrice: Double?,
    val maxPrice: Double?,
    val subcategories: List<String>
) {
    enum class FilterPriceType {
        ONLY_MIN_PRICE,
        ONLY_MAX_PRICE,
        BOTH_PRICES,
        NO_FILTER
    }

    fun isSet(): Boolean {
        return minPrice != null || maxPrice != null || subcategories.isNotEmpty()
    }

    fun filterType(): FilterPriceType {
        return when {
            minPrice != null && maxPrice != null -> FilterPriceType.BOTH_PRICES
            minPrice != null && maxPrice == null -> FilterPriceType.ONLY_MIN_PRICE
            maxPrice != null && minPrice == null -> FilterPriceType.ONLY_MAX_PRICE
            else -> FilterPriceType.NO_FILTER
        }
    }
}