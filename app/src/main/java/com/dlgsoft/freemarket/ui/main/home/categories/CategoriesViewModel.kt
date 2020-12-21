package com.dlgsoft.freemarket.ui.main.home.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlgsoft.freemarket.data.db.entities.Category
import com.dlgsoft.freemarket.data.repositories.CategoriesRepository
import kotlinx.coroutines.launch

class CategoriesViewModel(private val repository: CategoriesRepository) : ViewModel() {

    val categories = MutableLiveData<List<Category>>()

    fun getCategories() {
        viewModelScope.launch {
            categories.value = repository.getCategoriesDataFromCache()
        }
    }
}
