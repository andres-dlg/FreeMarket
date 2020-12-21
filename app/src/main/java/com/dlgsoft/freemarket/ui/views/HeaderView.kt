package com.dlgsoft.freemarket.ui.views

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.dlgsoft.freemarket.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class HeaderView(context: Context) : LinearLayout(context) {

    val text: TextView

    @ModelProp
    fun setText(stringResId: Int) {
        text.setText(stringResId)
    }

    @ModelProp
    fun setTextSize(dimenResId: Int) {
        text.textSize = context.resources.getDimension(dimenResId)
    }

    init {
        inflate(context, R.layout.header_view, this)
        text = findViewById(R.id.text)
    }
}