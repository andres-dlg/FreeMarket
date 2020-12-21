package com.dlgsoft.freemarket.network

import com.dlgsoft.freemarket.network.responses.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FreeMarketApi {
    @GET("/sites")
    suspend fun getSites(): Response<List<SiteResponse>>

    @GET("/sites/{site}/categories")
    suspend fun getCategories(@Path("site") site: String): Response<List<CategoryResponse>>

    @GET("/categories/{categoryId}")
    suspend fun getCategoryDetail(@Path("categoryId") categoryId: String): Response<CategoryDetailResponse>

    @GET("/sites/{site}/search")
    suspend fun getProductsForCategory(
        @Path("site") site: String,
        @Query("category") categoryId: String
    ): Response<ProductsResponse>

    // Ejemplo https://api.mercadolibre.com/items?ids=MLA862643954,MLA618949963&attributes={id,price,category_id,title}
    @GET("/items")
    suspend fun getProductsDetail(@Query("ids") ids: String): Response<List<ProductsDetailResponse>>

    @GET("/items/{id}")
    suspend fun getProductDetail(@Path("id") productId: String): Response<ProductsResponse>
}