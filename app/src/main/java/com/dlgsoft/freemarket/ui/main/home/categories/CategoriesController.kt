package com.dlgsoft.freemarket.ui.main.home.categories

import com.airbnb.epoxy.TypedEpoxyController
import com.dlgsoft.freemarket.data.db.entities.Category
import com.dlgsoft.freemarket.ui.views.categoryView
import com.dlgsoft.freemarket.ui.views.loadingView
import java.util.*

class CategoriesController(
    private val onClickListener: (category: Category) -> Unit
) : TypedEpoxyController<List<Category>>() {

    var isLoading: Boolean = true
    var originalData = listOf<Category>()

    override fun buildModels(data: List<Category>?) {
        if (isLoading) {
            loadingView {
                id("loading")
            }
        } else {
            data?.forEach {
                categoryView {
                    id(it.id)
                    name(it.name)
                    picture(it.pictureUrl)
                    listener { onClickListener.invoke(it) }
                }
            }
        }
    }

    /** Recibe un [newText] y filtra todas las categorias que contengan ese texto **/
    fun filterData(newText: String?) {
        if (newText.isNullOrBlank()) {
            setData(originalData)
        } else {
            val newDataSet = originalData.filter {
                it.name.toLowerCase(Locale.ROOT).contains(newText)
            }
            setData(newDataSet)
        }
    }
}