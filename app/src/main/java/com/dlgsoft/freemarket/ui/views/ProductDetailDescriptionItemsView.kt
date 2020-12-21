package com.dlgsoft.freemarket.ui.views

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.dlgsoft.freemarket.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ProductDetailDescriptionItemsView(context: Context) : LinearLayout(context) {

    private val headerText: TextView
    private val valueText: TextView

    @ModelProp
    fun setHeader(header: String) {
        headerText.text = header
    }

    @ModelProp
    fun setValue(value: String) {
        valueText.text = value
    }

    init {
        inflate(context, R.layout.product_item_description_items_view, this)
        headerText = findViewById(R.id.attribute_header)
        valueText = findViewById(R.id.attribute_value)
    }
}