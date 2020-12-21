package com.dlgsoft.freemarket.ui.main.profile

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlgsoft.freemarket.data.db.entities.Site
import com.dlgsoft.freemarket.data.repositories.SitesRepository
import com.dlgsoft.freemarket.utils.SharedPreferenceConstants.CURRENT_CURRENCY_CODE
import com.dlgsoft.freemarket.utils.SharedPreferenceConstants.CURRENT_SITE
import com.dlgsoft.freemarket.utils.SharedPreferenceConstants.CURRENT_SITE_DEFAULT
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: SitesRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val sites = MutableLiveData<Pair<List<Site>, String>>()

    fun getSites() {
        viewModelScope.launch {
            val currentSite = sharedPreferences.getString(CURRENT_SITE, CURRENT_SITE_DEFAULT) ?: ""
            val result = repository.getSitesFromCache()
            if (!result.isNullOrEmpty()) {
                sites.value = Pair(result, currentSite)
            }
        }
    }

    fun setRegion(regionCode: String?) {
        regionCode?.let {
            sharedPreferences.edit(commit = true) {
                putString(CURRENT_SITE, regionCode)
            }
        }
    }

    fun setCurrency(currencyCode: String?) {
        currencyCode?.let {
            sharedPreferences.edit(commit = true) {
                putString(CURRENT_CURRENCY_CODE, currencyCode)
            }
        }
    }
}