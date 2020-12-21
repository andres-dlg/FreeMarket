package com.dlgsoft.freemarket.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.dlgsoft.freemarket.data.db.dao.CategoryDao
import com.dlgsoft.freemarket.data.db.entities.Category
import com.dlgsoft.freemarket.data.db.entities.SubCategory
import com.dlgsoft.freemarket.extensions.noNetworkConnectivityError
import com.dlgsoft.freemarket.network.FreeMarketApi
import com.dlgsoft.freemarket.network.responses.CategoryDetailResponse
import com.dlgsoft.freemarket.utils.AppResult
import com.dlgsoft.freemarket.utils.NetworkManager.isOnline
import com.dlgsoft.freemarket.utils.SharedPreferenceConstants
import com.dlgsoft.freemarket.utils.handleApiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface CategoriesRepository {
    suspend fun getCategoriesFromNetwork(): AppResult<List<Category>>
    suspend fun getCategoriesDataFromCache(): List<Category>
}

class CategoriesRepositoryImpl(
    private val freeMarketApi: FreeMarketApi,
    private val context: Context,
    private val categoriesDao: CategoryDao,
    private val sharedPreferences: SharedPreferences
) : CategoriesRepository {

    /** Recupera las categorias y las guarda en la base de datos local.
     *
     * En caso de que no haya conexion a internet intenta recuperar las categorias previamente
     * recuperadas.**/
    override suspend fun getCategoriesFromNetwork(): AppResult<List<Category>> {
        val currentSite = sharedPreferences.getString(
            SharedPreferenceConstants.CURRENT_SITE,
            SharedPreferenceConstants.CURRENT_SITE_DEFAULT
        )
            ?: SharedPreferenceConstants.CURRENT_SITE_DEFAULT
        if (isOnline(context)) {
            return try {
                withContext(Dispatchers.IO) {
                    getCategories(currentSite)
                }
            } catch (e: Exception) {
                AppResult.Error(e)
            }
        } else {
            //check in db if the data exists
            val data = getCategoriesDataFromCache()
            return if (data.isNotEmpty()) {
                AppResult.Success(data)
            } else {
                //no network
                context.noNetworkConnectivityError()
            }
        }
    }

    /** Recupera las categorias desde la red **/
    private suspend fun getCategories(site: String): AppResult<List<Category>> {
        val response = freeMarketApi.getCategories(site)
        return if (response.isSuccessful) {
            //save the data
            val cachedData = arrayListOf<Category>()
            val data = response.body() ?: listOf()
            if (data.isNotEmpty()) {
                categoriesDao.clearCategories()
                categoriesDao.clearSubCategories()
                data.forEach {
                    getCategoryDetailAndSave(it.id)
                }
                cachedData.addAll(categoriesDao.getAllCategories())
            }
            AppResult.Success(cachedData)
        } else {
            handleApiError(response)
        }
    }

    /** Utiliza el Id de las categorias recuperadas anteriormente para traer el detalle de
     * cada categoría **/
    private suspend fun getCategoryDetailAndSave(categoryId: String) {
        val response = freeMarketApi.getCategoryDetail(categoryId)
        if (response.isSuccessful) {
            val data = response.body()
            saveCategories(data)
        } else {
            handleApiError(response)
        }
    }

    /** Guarda la categoria y sus subcategorias en la base de datos local **/
    private suspend fun saveCategories(data: CategoryDetailResponse?) {
        data?.let {
            val newCategory = Category().apply {
                name = data.name
                serverId = data.id
                pictureUrl = data.pictureUrl
                totalItems = data.totalItems
            }
            val cId = categoriesDao.insertCategory(newCategory)

            data.subcategories?.forEach { subcategory ->
                val newSubCategory = SubCategory().apply {
                    name = subcategory.name
                    serverId = subcategory.id
                    this.categoryId = cId
                }
                categoriesDao.insertSubCategory(newSubCategory)
            }
        }
    }

    /** Recupera todas las categorias de la base de datos local **/
    override suspend fun getCategoriesDataFromCache(): List<Category> {
        return withContext(Dispatchers.IO) {
            categoriesDao.getAllCategories()
        }
    }
}
