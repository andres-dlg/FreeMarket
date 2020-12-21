package com.dlgsoft.freemarket.di

import com.dlgsoft.freemarket.BuildConfig
import com.dlgsoft.freemarket.network.FreeMarketApi
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Provee objetos dedicados a la creacion del cliente Http y de la API **/
@JvmField
val networkModule = module {
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_PATH)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()
    }

    fun provideApi(retrofit: Retrofit): FreeMarketApi =
        retrofit.create(FreeMarketApi::class.java)

    factory { provideOkHttpClient() }
    factory { provideApi(get()) }
    single { provideRetrofit(get()) }
}
