package com.dlgsoft.freemarket.network.responses

import com.google.gson.annotations.SerializedName

data class SiteResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("default_currency_id")
    val defaultCurrencyCode: String
)