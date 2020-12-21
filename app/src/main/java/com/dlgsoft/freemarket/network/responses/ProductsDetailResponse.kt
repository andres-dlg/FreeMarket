package com.dlgsoft.freemarket.network.responses

import com.google.gson.annotations.SerializedName

data class ProductsDetailResponse(
    @SerializedName("body")
    val body: ProductsResults
) {
    data class ProductsResults(
        @SerializedName("id")
        val id: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("price")
        val price: Double,
        @SerializedName("currency_id")
        val currencyId: String,
        @SerializedName("thumbnail")
        val thumbnail: String,
        @SerializedName("permalink")
        val link: String,
        @SerializedName("condition")
        val condition: String,
        @SerializedName("seller_address")
        val address: ProductAddress,
        @SerializedName("attributes")
        val attributes: List<ProductAttribute>?,
        @SerializedName("pictures")
        val pictures: List<ProductPicture>?
    )

    data class ProductAddress(
        @SerializedName("city")
        val city: SellerCity,
        @SerializedName("state")
        val state: SellerState
    )

    data class SellerCity(
        @SerializedName("name")
        val name: String
    )

    data class SellerState(
        @SerializedName("name")
        val name: String
    )

    data class ProductAttribute(
        @SerializedName("name")
        val name: String,
        @SerializedName("value_name")
        val value: String
    )

    data class ProductPicture(
        @SerializedName("secure_url")
        val url: String,
    )
}


