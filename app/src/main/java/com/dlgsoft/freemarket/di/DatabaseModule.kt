package com.dlgsoft.freemarket.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.dlgsoft.freemarket.data.db.FreeMarketDatabase
import com.dlgsoft.freemarket.data.db.dao.*
import com.dlgsoft.freemarket.utils.SharedPreferenceConstants.SP_FREEMARKET
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/** Provee y genera las instancias de lo referido a la DB local: Database, Dao y SharedPreferences **/
val databaseModule = module {

    fun provideDatabase(application: Application): FreeMarketDatabase {
        return Room.databaseBuilder(application, FreeMarketDatabase::class.java, "free_market_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideCategoryDao(database: FreeMarketDatabase): CategoryDao {
        return database.categoryDao
    }

    fun provideProductDao(database: FreeMarketDatabase): ProductDao {
        return database.productDao
    }

    fun provideProductAttributesDao(database: FreeMarketDatabase): ProductAttributeDao {
        return database.productAttributeDao
    }

    fun provideProductPictureDao(database: FreeMarketDatabase): ProductPictureDao {
        return database.productPictureDao
    }

    fun provideSiteDao(database: FreeMarketDatabase): SiteDao {
        return database.siteDao
    }

    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(SP_FREEMARKET, Context.MODE_PRIVATE)
    }

    single { provideDatabase(androidApplication()) }
    single { provideCategoryDao(get()) }
    single { provideProductDao(get()) }
    single { provideProductAttributesDao(get()) }
    single { provideProductPictureDao(get()) }
    single { provideSiteDao(get()) }
    single { provideSharedPreferences(androidApplication()) }
}
