package com.dlgsoft.freemarket.ui.product

import com.airbnb.epoxy.TypedEpoxyController
import com.dlgsoft.freemarket.R
import com.dlgsoft.freemarket.data.db.relations.ProductWithAttributes
import com.dlgsoft.freemarket.extensions.roundIfNotDecimal
import com.dlgsoft.freemarket.ui.views.headerView
import com.dlgsoft.freemarket.ui.views.productDetailDescriptionItemsView
import com.dlgsoft.freemarket.ui.views.productDetailHeader
import com.dlgsoft.freemarket.utils.FreeMarketMapper
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class ProductDetailController : TypedEpoxyController<ProductWithAttributes>(), KoinComponent {

    private val mapper: FreeMarketMapper by inject()

    override fun buildModels(product: ProductWithAttributes?) {
        product?.let {
            productDetailHeader {
                id("header")
                name(it.product.title ?: "")
                price(
                    "${
                        mapper.getCurrencySymbol(it.product.currencyId ?: "")
                    } ${
                        it.product.price?.roundIfNotDecimal()
                    }"
                )
                address(it.product.address ?: "")
                condition(mapper.getCondition(it.product.condition ?: ""))
            }

            if (it.attributes.isNotEmpty()) {
                headerView {
                    id("description_title")
                    text(R.string.description)
                    textSize(R.dimen.product_detail_description_header_size)
                }
                it.attributes.forEach { attr ->
                    if (!attr.name.isNullOrBlank() && !attr.value.isNullOrBlank())
                        productDetailDescriptionItemsView {
                            id(attr.id)
                            header(attr.name)
                            value(attr.value)
                        }
                }
            }
        }
    }
}