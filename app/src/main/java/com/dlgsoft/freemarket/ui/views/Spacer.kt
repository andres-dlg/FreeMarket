package com.dlgsoft.freemarket.ui.views

import android.content.Context
import android.widget.LinearLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class Spacer(context: Context) : LinearLayout(context) {

    @ModelProp
    fun setHeight(heightResId: Int) {
        layoutParams.height = context.resources.getDimensionPixelSize(heightResId)
    }
}