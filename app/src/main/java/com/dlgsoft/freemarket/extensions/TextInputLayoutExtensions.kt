package com.dlgsoft.freemarket.extensions

import com.google.android.material.textfield.TextInputLayout

// Si se muestra un error en un TextInputLayout y luego se oculta, el espacio que ocupo el mensaje
// de error no se limpiará a menos que se seteen isErrorEnabled en true o false justo antes y
// despues de setear u ocultar el error respectivamente.

fun TextInputLayout.setCustomError(errorResId: Int) {
    isErrorEnabled = true
    error = this.context.getString(errorResId)
}

fun TextInputLayout.clearError() {
    error = null
    isErrorEnabled = false
}