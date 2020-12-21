package com.dlgsoft.freemarket.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.dlgsoft.freemarket.data.db.dao.ProductAttributeDao
import com.dlgsoft.freemarket.data.db.dao.ProductDao
import com.dlgsoft.freemarket.data.db.dao.ProductPictureDao
import com.dlgsoft.freemarket.data.db.entities.Product
import com.dlgsoft.freemarket.data.db.entities.ProductAttribute
import com.dlgsoft.freemarket.data.db.entities.ProductPicture
import com.dlgsoft.freemarket.data.db.entities.SubCategory
import com.dlgsoft.freemarket.data.db.relations.ProductWithAttributes
import com.dlgsoft.freemarket.extensions.noNetworkConnectivityError
import com.dlgsoft.freemarket.network.FreeMarketApi
import com.dlgsoft.freemarket.network.responses.ProductsDetailResponse
import com.dlgsoft.freemarket.utils.AppResult
import com.dlgsoft.freemarket.utils.NetworkManager.isOnline
import com.dlgsoft.freemarket.utils.SharedPreferenceConstants.CURRENT_SITE
import com.dlgsoft.freemarket.utils.SharedPreferenceConstants.CURRENT_SITE_DEFAULT
import com.dlgsoft.freemarket.utils.handleApiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ProductsRepository {
    suspend fun getProductsIdsForCategoryId(categoryId: Long): List<String>
    suspend fun getProductsForCategoryId(
        categoryId: Long,
        categoryServerId: String
    ): AppResult<List<ProductWithAttributes>>

    suspend fun getProductById(productId: Long): ProductWithAttributes?
    suspend fun getSubCategoriesForCategoryId(categoryId: Long): List<SubCategory>
}

class ProductsRepositoryImpl(
    private val freeMarketApi: FreeMarketApi,
    private val context: Context,
    private val productsDao: ProductDao,
    private val productAttributeDao: ProductAttributeDao,
    private val productPictureDao: ProductPictureDao,
    private val sharedPreferences: SharedPreferences
) : ProductsRepository {

    /** Recupera los productos y los guarda en la base de datos local.
     *
     * En caso de que no haya conexion a internet devuelve un error.**/
    override suspend fun getProductsForCategoryId(
        categoryId: Long,
        categoryServerId: String
    ): AppResult<List<ProductWithAttributes>> {
        if (isOnline(context)) {
            val currentSite = sharedPreferences.getString(CURRENT_SITE, CURRENT_SITE_DEFAULT)
                ?: CURRENT_SITE_DEFAULT
            val productsResponse =
                freeMarketApi.getProductsForCategory(currentSite, categoryServerId)
            return try {
                if (productsResponse.isSuccessful) {
                    val cachedData = arrayListOf<ProductWithAttributes>()
                    val data = productsResponse.body()
                    if (data != null) {
                        val newProducts = getProductsDetail(data.results.map { it.id }, categoryId)
                        cachedData.addAll(newProducts)
                    }
                    AppResult.Success(cachedData)
                } else {
                    handleApiError(productsResponse)
                }
            } catch (e: Exception) {
                AppResult.Error(e)
            }
        } else {
            return context.noNetworkConnectivityError()
        }
    }

    /** Recupera el detalle para cada uno de los ids de los productos recibidos por parametro
     * en [productIds] **/
    private suspend fun getProductsDetail(
        productIds: List<String>,
        categoryId: Long
    ): List<ProductWithAttributes> {
        val finalProductsIds = generateIdsList(productIds)
        withContext(Dispatchers.IO) {
            productAttributeDao.clearData()
            productPictureDao.clearData()
            productsDao.clearData()
        }
        finalProductsIds.forEach { pIds ->
            val ids = pIds.joinToString(",")
            val response = freeMarketApi.getProductsDetail(ids)
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    withContext(Dispatchers.IO) {
                        data.forEach {
                            val newProductId = insertProduct(it.body, categoryId)
                            if (newProductId != 0L) {
                                it.body.attributes?.let { attrs ->
                                    insertAttributes(attrs, newProductId)
                                }
                                it.body.pictures?.let { pictures ->
                                    insertPictures(pictures, newProductId)
                                }
                            }
                        }
                        productsDao.getAllProductsByCategoryId(categoryId)
                    }
                } else {
                    emptyList()
                }
            } else {
                handleApiError(response)
                emptyList()
            }
        }

        return withContext(Dispatchers.IO) {
            productsDao.getAllProductsByCategoryId(categoryId)
        }
    }

    /** Genera una lista de listas de ids. Cada lista sera utilizada para hacer las peticiones
     * correspondientes.
     *
     * Esto es necesario ya que la API de ML solo soporta hasta 20 Ids al mismo tiempo. Es por eso que se
     * debe partir la lista original de Ids en diversas listas de 20 como máximo. **/
    private fun generateIdsList(productIds: List<String>): List<List<String>> {
        val finalList = arrayListOf<List<String>>()
        val tempList = arrayListOf<String>()
        productIds.forEach { productId ->
            if (tempList.size < 20) {
                tempList.add(productId)
            } else {
                finalList.add(tempList.toList())
                tempList.clear()
            }
        }
        if (tempList.size > 0) {
            finalList.add(tempList.toList())
        }
        return finalList
    }

    /** Guarda un nuevo producto en la base de datos local **/
    private fun insertProduct(result: ProductsDetailResponse.ProductsResults, cId: Long): Long {
        return try {
            productsDao.insertProduct(
                Product().apply {
                    title = result.title
                    serverId = result.id
                    thumbnail = result.thumbnail
                    link = result.link
                    currencyId = result.currencyId
                    condition = result.condition
                    price = result.price
                    address = "${result.address.city.name}, ${result.address.state.name}"
                    categoryId = cId
                }
            )
        } catch (e: java.lang.Exception) {
            return 0L
        }
    }

    /** Guarda los atributos de un producto en la base de datos local **/
    private fun insertAttributes(
        attrs: List<ProductsDetailResponse.ProductAttribute>,
        pId: Long
    ) {
        attrs.forEach {
            val attr = ProductAttribute(
                0L,
                it.name,
                it.value,
                pId
            )
            try {
                productAttributeDao.insertProductAttribute(attr)
            } catch (e: java.lang.Exception) {
                print(e)
            }
        }
    }

    /** Guarda las fotos de un producto en la base de datos local **/
    private fun insertPictures(
        pictures: List<ProductsDetailResponse.ProductPicture>,
        pId: Long
    ) {
        pictures.forEach {
            val pic = ProductPicture(
                0L,
                it.url,
                pId
            )
            try {
                productPictureDao.insertProductPicture(pic)
            } catch (e: java.lang.Exception) {
                print(e)
            }
        }
    }

    /** Recupera un producto por su Id de la base de datos local **/
    override suspend fun getProductById(productId: Long): ProductWithAttributes {
        return withContext(Dispatchers.IO) {
            productsDao.getById(productId)
        }
    }

    /** Recupera las subcategorias de una categoria por su Id de la base de datos local **/
    override suspend fun getSubCategoriesForCategoryId(categoryId: Long): List<SubCategory> {
        return withContext(Dispatchers.IO) {
            productsDao.getSubCategoriesForCategoryId(categoryId)
        }
    }

    /** Recupera una lista de los serverIds de la base de datos local **/
    override suspend fun getProductsIdsForCategoryId(categoryId: Long): List<String> {
        return withContext(Dispatchers.IO) {
            productsDao.getProductsIdsForCategoryId(categoryId)
        }
    }
}