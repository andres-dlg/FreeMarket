package com.dlgsoft.freemarket.ui.main.home.products

import com.airbnb.epoxy.TypedEpoxyController
import com.dlgsoft.freemarket.data.db.relations.ProductWithAttributes
import com.dlgsoft.freemarket.extensions.roundIfNotDecimal
import com.dlgsoft.freemarket.ui.views.loadingView
import com.dlgsoft.freemarket.ui.views.productItemView
import com.dlgsoft.freemarket.utils.FreeMarketMapper
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class ProductsController(
    private val onClickListener: (productId: Long) -> Unit
) : TypedEpoxyController<List<ProductWithAttributes>>(), KoinComponent {

    private val mapper: FreeMarketMapper by inject()

    var isLoading: Boolean = true

    override fun buildModels(data: List<ProductWithAttributes>?) {
        if (isLoading) {
            loadingView {
                id("loading")
            }
        } else {
            data?.forEach {
                productItemView {
                    id(it.product.id)
                    name(it.product.title ?: "")
                    price(
                        "${
                            mapper.getCurrencySymbol(it.product.currencyId ?: "")
                        } ${
                            it.product.price?.roundIfNotDecimal()
                        }"
                    )
                    image(if (!it.pictures.isNullOrEmpty()) it.pictures[0].url ?: "" else "")
                    listener { onClickListener.invoke(it.product.id) }
                }
            }
        }
    }
}
