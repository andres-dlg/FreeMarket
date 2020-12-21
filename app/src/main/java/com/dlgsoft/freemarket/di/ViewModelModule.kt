package com.dlgsoft.freemarket.di

import com.dlgsoft.freemarket.ui.main.home.categories.CategoriesViewModel
import com.dlgsoft.freemarket.ui.main.home.products.ProductsViewModel
import com.dlgsoft.freemarket.ui.main.profile.ProfileViewModel
import com.dlgsoft.freemarket.ui.product.ProductDetailViewModel
import com.dlgsoft.freemarket.ui.splash.SplashViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/** Provee de los ViewModel a las respectivas pantallas */
@JvmField
val viewModelModule = module {
    viewModel {
        SplashViewModel(
            categoriesRepository = get(),
            sitesRepository = get(),
            sharedPreferences = get()
        )
    }
    viewModel { CategoriesViewModel(repository = get()) }
    viewModel { ProductsViewModel(repository = get(), get()) }
    viewModel { ProductDetailViewModel(repository = get()) }
    viewModel { ProfileViewModel(repository = get(), get()) }
}