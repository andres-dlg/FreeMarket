package com.dlgsoft.freemarket.di

import android.content.Context
import android.content.SharedPreferences
import com.dlgsoft.freemarket.data.db.dao.*
import com.dlgsoft.freemarket.data.repositories.*
import com.dlgsoft.freemarket.network.FreeMarketApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** Provee de los repositorios que seran utilizados para recuperar los datos tanto de la red como
 * de la base de datos local **/
val repositoryModule = module {
    fun provideCategoriesRepository(
        api: FreeMarketApi,
        context: Context,
        dao: CategoryDao,
        sharedPreferences: SharedPreferences
    ): CategoriesRepository {
        return CategoriesRepositoryImpl(api, context, dao, sharedPreferences)
    }

    fun provideProductsRepository(
        api: FreeMarketApi,
        context: Context,
        productDao: ProductDao,
        attributeDao: ProductAttributeDao,
        pictureDao: ProductPictureDao,
        sharedPreferences: SharedPreferences
    ): ProductsRepository {
        return ProductsRepositoryImpl(
            api,
            context,
            productDao,
            attributeDao,
            pictureDao,
            sharedPreferences
        )
    }

    fun provideSitesRepository(
        api: FreeMarketApi,
        context: Context,
        siteDao: SiteDao,
    ): SitesRepository {
        return SitesRepositoryImpl(api, context, siteDao)
    }

    single { provideProductsRepository(get(), androidContext(), get(), get(), get(), get()) }
    single { provideCategoriesRepository(get(), androidContext(), get(), get()) }
    single { provideSitesRepository(get(), androidContext(), get()) }
}