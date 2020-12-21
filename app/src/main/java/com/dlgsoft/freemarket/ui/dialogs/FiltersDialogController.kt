package com.dlgsoft.freemarket.ui.dialogs

import com.airbnb.epoxy.EpoxyController
import com.dlgsoft.freemarket.R
import com.dlgsoft.freemarket.data.db.entities.SubCategory
import com.dlgsoft.freemarket.extensions.roundIfNotDecimal
import com.dlgsoft.freemarket.ui.views.headerView
import com.dlgsoft.freemarket.ui.views.priceView
import com.dlgsoft.freemarket.ui.views.spacer
import com.dlgsoft.freemarket.ui.views.subCategoryView
import com.dlgsoft.freemarket.utils.FreeMarketMapper
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class FiltersDialogController : EpoxyController(), KoinComponent {

    // IMPORTANTE:
    // El filtro de subcategorias no funciona por falta de tiempo (error de estimación)

    private val mapper: FreeMarketMapper by inject()

    var currencyCode: String = ""
    var subCategories = listOf<SubCategory>()

    private var selectedSubCategoriesIds = mutableListOf<String>()
    private var minPrice: Double? = null
    private var maxPrice: Double? = null
    private var isValid: Boolean = false

    override fun buildModels() {
        headerView {
            id("price_title")
            text(R.string.price)
            textSize(R.dimen.filter_header_size)
        }
        priceView {
            id("price_filter")
            currencySymbol(mapper.getCurrencySymbol(currencyCode))
            minPrice(
                if (minPrice == null) {
                    ""
                } else {
                    minPrice?.roundIfNotDecimal().toString()
                }
            )
            maxPrice(
                if (maxPrice == null) {
                    ""
                } else {
                    maxPrice?.roundIfNotDecimal().toString()
                }
            )
            validPriceListener { isValid = it }
            maxPriceListener { maxPrice = it }
            minPriceListener { minPrice = it }
        }
        if (subCategories.isNotEmpty()) {
            spacer {
                id("spacer")
                height(R.dimen.space_height_16dp)
            }
            headerView {
                id("subcategories_filter")
                text(R.string.subcategories)
                textSize(R.dimen.filter_header_size)
            }
            subCategories.forEach {
                subCategoryView {
                    id(it.hashCode())
                    subCategoryName(it.name)
                    isChecked(selectedSubCategoriesIds.contains(it.serverId))
                    onCheckedListener { isChecked ->
                        if (isChecked) {
                            if (!selectedSubCategoriesIds.contains(it.serverId)) {
                                selectedSubCategoriesIds.add(it.serverId)
                            }
                        } else {
                            if (selectedSubCategoriesIds.contains(it.serverId)) {
                                selectedSubCategoriesIds.remove(it.serverId)
                            }
                        }
                        requestModelBuild()
                    }
                }
            }
        }
    }

    fun getAppliedFilter(): Filter? {
        return if (isValid) {
            return Filter(
                minPrice,
                maxPrice,
                selectedSubCategoriesIds
            )
        } else {
            null
        }
    }

    fun setCurrentFilter(currentFilter: Filter) {
        minPrice = currentFilter.minPrice
        maxPrice = currentFilter.maxPrice
        selectedSubCategoriesIds = currentFilter.subcategories.toMutableList()
    }
}