package com.dlgsoft.freemarket.ui.views

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.dlgsoft.freemarket.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ProductDetailHeader(context: Context) : LinearLayout(context) {

    private val name: TextView
    private val price: TextView
    private val condition: TextView
    private val address: TextView

    @ModelProp
    fun setName(productName: String) {
        name.text = productName
    }

    @ModelProp
    fun setPrice(productPrice: String) {
        price.text = productPrice
    }

    @ModelProp
    fun setCondition(productCondition: String) {
        condition.text = productCondition
    }

    @ModelProp
    fun setAddress(productAddress: String) {
        address.text = productAddress
    }

    init {
        inflate(context, R.layout.product_detail_header, this)
        name = findViewById(R.id.name)
        price = findViewById(R.id.price)
        condition = findViewById(R.id.condition)
        address = findViewById(R.id.address)
    }
}