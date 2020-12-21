package com.dlgsoft.freemarket.network.responses

import com.google.gson.annotations.SerializedName

data class ProductsResponse(
    @SerializedName("results")
    val results: List<ProductsResults>
) {
    data class ProductsResults(
        @SerializedName("id")
        val id: String
    )
}
