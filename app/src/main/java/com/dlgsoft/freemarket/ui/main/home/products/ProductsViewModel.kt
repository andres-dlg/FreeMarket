package com.dlgsoft.freemarket.ui.main.home.products

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlgsoft.freemarket.data.db.entities.SubCategory
import com.dlgsoft.freemarket.data.db.relations.ProductWithAttributes
import com.dlgsoft.freemarket.data.repositories.ProductsRepository
import com.dlgsoft.freemarket.ui.dialogs.Filter
import com.dlgsoft.freemarket.utils.AppResult
import com.dlgsoft.freemarket.utils.SharedPreferenceConstants.CURRENT_CURRENCY_CODE
import com.dlgsoft.freemarket.utils.SharedPreferenceConstants.CURRENT_CURRENCY_CODE_DEFAULT
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val repository: ProductsRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val products = MutableLiveData<List<ProductWithAttributes>>()
    val showError = MutableLiveData<String>()

    lateinit var subCategories: List<SubCategory>
    lateinit var currencyCode: String

    private var currentProducts = listOf<ProductWithAttributes>()
    var currentFilter = Filter(
        null,
        null,
        listOf()
    )

    fun getProducts(categoryServerId: String, categoryId: Long) {
        viewModelScope.launch {
            when (val result = repository.getProductsForCategoryId(categoryId, categoryServerId)) {
                is AppResult.Success -> {
                    currentProducts = result.successData
                    products.value = currentProducts
                }
                is AppResult.Error -> {
                    showError.value = result.exception.message
                }
            }
        }
    }

    fun getFiltersData(categoryId: Long) {
        viewModelScope.launch {
            currencyCode =
                sharedPreferences.getString(CURRENT_CURRENCY_CODE, CURRENT_CURRENCY_CODE_DEFAULT)
                    ?: CURRENT_CURRENCY_CODE_DEFAULT
            subCategories = repository.getSubCategoriesForCategoryId(categoryId)
        }
    }

    fun getProductsForAppliedFilter(filter: Filter) {
        // TODO: Falta implementar el filtro para las subcategorias
        currentFilter = filter
        val filteredProducts = when (filter.filterType()) {
            Filter.FilterPriceType.ONLY_MIN_PRICE -> {
                currentProducts.filter { p ->
                    p.product.price != null && p.product.price!! >= filter.minPrice!!
                }
            }
            Filter.FilterPriceType.ONLY_MAX_PRICE -> {
                currentProducts.filter { p ->
                    p.product.price != null && p.product.price!! <= filter.maxPrice!!
                }
            }
            Filter.FilterPriceType.BOTH_PRICES -> {
                currentProducts.filter { p ->
                    p.product.price != null &&
                            p.product.price!! <= filter.maxPrice!! &&
                            p.product.price!! >= filter.minPrice!!
                }
            }
            Filter.FilterPriceType.NO_FILTER -> {
                currentProducts
            }
        }
        products.value = filteredProducts
    }
}