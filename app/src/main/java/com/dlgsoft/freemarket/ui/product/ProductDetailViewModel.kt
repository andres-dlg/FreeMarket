package com.dlgsoft.freemarket.ui.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlgsoft.freemarket.R
import com.dlgsoft.freemarket.data.db.relations.ProductWithAttributes
import com.dlgsoft.freemarket.data.repositories.ProductsRepository
import kotlinx.coroutines.launch

class ProductDetailViewModel(private val repository: ProductsRepository) : ViewModel() {

    var productLink = ""
    val product = MutableLiveData<ProductWithAttributes>()
    val showError = MutableLiveData<Int>()

    fun getProduct(productId: Long) {
        viewModelScope.launch {
            val result = repository.getProductById(productId)
            if (result != null) {
                productLink = result.product.link ?: ""
                product.value = result
            } else {
                showError.value = R.string.product_error
            }
        }
    }
}