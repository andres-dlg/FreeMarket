package com.dlgsoft.freemarket.ui.views

import android.content.Context
import android.widget.CheckBox
import android.widget.LinearLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.dlgsoft.freemarket.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SubCategoryView(context: Context) : LinearLayout(context) {

    private var isUserInteracting = false

    private val checkBox: CheckBox

    @ModelProp
    fun setSubCategoryName(name: String) {
        checkBox.text = name
    }

    @ModelProp
    fun setIsChecked(isChecked: Boolean) {
        checkBox.isChecked = isChecked
    }

    @CallbackProp
    fun setOnCheckedListener(listener: ((isChecked: Boolean) -> Unit)?) {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isUserInteracting) {
                listener?.invoke(isChecked)
            }
        }
    }

    @AfterPropsSet
    fun finishSetup() {
        isUserInteracting = true
    }

    init {
        inflate(context, R.layout.subcategory_view, this)
        checkBox = findViewById(R.id.filter_check)
    }
}