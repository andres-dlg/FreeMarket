package com.dlgsoft.freemarket.ui.views

import android.content.Context
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.dlgsoft.freemarket.R
import com.dlgsoft.freemarket.extensions.clearError
import com.dlgsoft.freemarket.extensions.isNumeric
import com.dlgsoft.freemarket.extensions.setCustomError
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class PriceView(context: Context) : LinearLayout(context) {

    private val minPriceInputLayout: TextInputLayout
    private val maxPriceInputLayout: TextInputLayout
    private val minPriceInputEditText: TextInputEditText
    private val maxPriceInputEditText: TextInputEditText

    private var isValidListener: ((Boolean) -> Unit)? = null
    private var minPriceListener: ((Double?) -> Unit)? = null
    private var maxPriceListener: ((Double?) -> Unit)? = null

    @ModelProp
    fun setCurrencySymbol(symbol: String) {
        val prefix = "$symbol "
        minPriceInputLayout.prefixText = prefix
        maxPriceInputLayout.prefixText = prefix
    }

    @ModelProp
    fun setMinPrice(minPrice: String) {
        minPriceInputEditText.setText(minPrice)
    }

    @ModelProp
    fun setMaxPrice(maxPrice: String) {
        maxPriceInputEditText.setText(maxPrice)
    }

    @CallbackProp
    fun setValidPriceListener(isValidListener: ((Boolean) -> Unit)?) {
        this.isValidListener = isValidListener
    }

    @CallbackProp
    fun setMinPriceListener(minPriceListener: ((Double?) -> Unit)?) {
        this.minPriceListener = minPriceListener
    }

    @CallbackProp
    fun setMaxPriceListener(maxPriceListener: ((Double?) -> Unit)?) {
        this.maxPriceListener = maxPriceListener
    }

    init {
        inflate(context, R.layout.price_view, this)
        minPriceInputLayout = findViewById(R.id.input_layout_min_price)
        maxPriceInputLayout = findViewById(R.id.input_layout_max_price)
        minPriceInputEditText = findViewById(R.id.input_min_price)
        maxPriceInputEditText = findViewById(R.id.input_max_price)

        minPriceInputEditText.addTextChangedListener {
            validatePrices()
        }

        maxPriceInputEditText.addTextChangedListener {
            validatePrices()
        }
    }

    private fun validatePrices() {
        // Actualizo las variables en el controller
        val minPriceString = if (minPriceInputEditText.text.isNullOrBlank()) {
            minPriceListener?.invoke(null)
            ""
        } else {
            minPriceInputEditText.text.toString()
        }

        val maxPriceString = if (maxPriceInputEditText.text.isNullOrBlank()) {
            maxPriceListener?.invoke(null)
            ""
        } else {
            maxPriceInputEditText.text.toString()
        }

        if (minPriceString.isNumeric()) {
            minPriceListener?.invoke(minPriceString.toDouble())
        }

        if (maxPriceString.isNumeric()) {
            maxPriceListener?.invoke(maxPriceString.toDouble())
        }

        // Valido los inputs para mostrar (o no) los errores
        val canValidate = minPriceString.isNotBlank() &&
                minPriceString.isNumeric() &&
                maxPriceString.isNotBlank() &&
                maxPriceString.isNumeric()

        if (canValidate) {
            val minPrice = minPriceString.toFloat()
            val maxPrice = maxPriceString.toFloat()
            when {
                minPriceInputEditText.isFocused && minPrice > maxPrice -> {
                    isValidListener?.invoke(false)
                    minPriceInputLayout.setCustomError(R.string.min_price_error)
                }
                maxPriceInputEditText.isFocused && maxPrice < minPrice -> {
                    isValidListener?.invoke(false)
                    maxPriceInputLayout.setCustomError(R.string.max_price_error)
                }
                minPrice <= maxPrice || maxPrice >= minPrice -> clearErrors()
            }
        } else {
            clearErrors()
        }
    }

    private fun clearErrors() {
        isValidListener?.invoke(true)
        minPriceInputLayout.clearError()
        maxPriceInputLayout.clearError()
    }
}