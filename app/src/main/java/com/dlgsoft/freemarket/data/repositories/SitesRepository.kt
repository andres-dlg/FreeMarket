package com.dlgsoft.freemarket.data.repositories

import android.content.Context
import com.dlgsoft.freemarket.data.db.dao.SiteDao
import com.dlgsoft.freemarket.data.db.entities.Site
import com.dlgsoft.freemarket.extensions.noNetworkConnectivityError
import com.dlgsoft.freemarket.network.FreeMarketApi
import com.dlgsoft.freemarket.utils.AppResult
import com.dlgsoft.freemarket.utils.NetworkManager
import com.dlgsoft.freemarket.utils.handleApiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface SitesRepository {
    suspend fun getSites(): AppResult<List<Site>>
    suspend fun getSitesFromCache(): List<Site>
}

class SitesRepositoryImpl(
    private val freeMarketApi: FreeMarketApi,
    private val context: Context,
    private val sitesDao: SiteDao
) : SitesRepository {

    /** Recupera los sites y los guarda en la base de datos local.
     *
     * En caso de que no haya conexión a internet intenta recuperar los sitios previamente
     * recuperadas.
     *
     * En caso de que no haya conexión y no encuentre sitios en la DB local, arroja un error.
     * **/
    override suspend fun getSites(): AppResult<List<Site>> {
        if (NetworkManager.isOnline(context)) {
            return try {
                val response = freeMarketApi.getSites()
                return if (response.isSuccessful) {
                    //save the data
                    val cachedData = arrayListOf<Site>()
                    val data = response.body() ?: listOf()
                    if (data.isNotEmpty()) {
                        withContext(Dispatchers.IO) {
                            sitesDao.clearData()
                            data.forEach {
                                val site = Site(
                                    0,
                                    it.id,
                                    it.name,
                                    it.defaultCurrencyCode
                                )
                                sitesDao.insertSite(site)
                            }
                            cachedData.addAll(getSitesFromCache())
                        }
                    }
                    AppResult.Success(cachedData)
                } else {
                    handleApiError(response)
                }
            } catch (e: Exception) {
                AppResult.Error(e)
            }
        } else {
            //check in db if the data exists
            val data = getSitesFromCache()
            return if (data.isNotEmpty()) {
                AppResult.Success(data)
            } else {
                //no network
                context.noNetworkConnectivityError()
            }
        }
    }

    /** Recupera todos los sites de la DB local **/
    override suspend fun getSitesFromCache(): List<Site> {
        return withContext(Dispatchers.IO) {
            sitesDao.getAllSites()
        }
    }
}