package com.dlgsoft.freemarket.di

import android.content.Context
import com.dlgsoft.freemarket.utils.FreeMarketMapper
import com.squareup.picasso.Picasso
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** Provee objetos que pueden ser utilizados en cualquier parte de la aplicación */
@JvmField
val commonModule = module {
    fun providePicassoInstance(context: Context) = Picasso.Builder(context).build()
    single { FreeMarketMapper() }
    single { providePicassoInstance(androidContext()) }
}
