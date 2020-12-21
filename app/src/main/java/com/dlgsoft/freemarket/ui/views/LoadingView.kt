package com.dlgsoft.freemarket.ui.views

import android.content.Context
import android.widget.LinearLayout
import com.airbnb.epoxy.ModelView
import com.dlgsoft.freemarket.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
class LoadingView(context: Context) : LinearLayout(context) {
    init {
        inflate(context, R.layout.loading_view, this)
    }
}