package com.dlgsoft.freemarket.ui.splash

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlgsoft.freemarket.data.repositories.CategoriesRepository
import com.dlgsoft.freemarket.data.repositories.SitesRepository
import com.dlgsoft.freemarket.utils.AppResult
import com.dlgsoft.freemarket.utils.SharedPreferenceConstants.CURRENT_CURRENCY_CODE
import com.dlgsoft.freemarket.utils.SharedPreferenceConstants.CURRENT_CURRENCY_CODE_DEFAULT
import com.dlgsoft.freemarket.utils.SharedPreferenceConstants.CURRENT_SITE
import com.dlgsoft.freemarket.utils.SharedPreferenceConstants.CURRENT_SITE_DEFAULT
import kotlinx.coroutines.launch

class SplashViewModel(
    private val categoriesRepository: CategoriesRepository,
    private val sitesRepository: SitesRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val showLoading = MutableLiveData<Boolean>()
    val onSuccess = MutableLiveData<Boolean>()
    val onError = MutableLiveData<String>()

    fun getInitialConfig() {
        viewModelScope.launch {
            showLoading.value = true
            getSites()
        }
    }

    private suspend fun getSites() {
        // Se recuperan los sites y se guardan.
        when (val result = sitesRepository.getSites()) {
            is AppResult.Success -> getCategories()
            is AppResult.Error -> {
                showLoading.value = false
                onError.value = result.exception.message
            }
        }
    }

    private suspend fun getCategories() {
        // Se revisa si ya hay un site guardado. Si no hay ninguno, se setea el de Argentina por defecto.
        val site = sharedPreferences.getString(CURRENT_SITE, "") ?: ""
        if (site.isEmpty()) {
            sharedPreferences.edit(commit = true) {
                putString(
                    CURRENT_SITE,
                    CURRENT_SITE_DEFAULT
                )
            }
            sharedPreferences.edit(commit = true) {
                putString(
                    CURRENT_CURRENCY_CODE,
                    CURRENT_CURRENCY_CODE_DEFAULT
                )
            }
        }

        // Se recuperan las categorias para la categoria seleccionada.
        when (val result = categoriesRepository.getCategoriesFromNetwork()) {
            is AppResult.Success -> {
                showLoading.value = true
                onSuccess.value = true
            }
            is AppResult.Error -> {
                showLoading.value = false
                onError.value = result.exception.message
            }
        }
    }
}