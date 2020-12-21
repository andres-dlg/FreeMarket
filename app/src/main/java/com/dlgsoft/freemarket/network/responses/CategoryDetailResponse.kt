package com.dlgsoft.freemarket.network.responses

import com.google.gson.annotations.SerializedName

data class CategoryDetailResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("picture")
    val pictureUrl: String,
    @SerializedName("total_items_in_this_category")
    val totalItems: Int,
    @SerializedName("children_categories")
    val subcategories: List<SubCategoryResponse>?
) {
    data class SubCategoryResponse(
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("total_items_in_this_category")
        val totalItems: Int
    )
}