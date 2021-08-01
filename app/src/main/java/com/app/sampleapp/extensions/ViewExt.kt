package com.app.sampleapp.extensions

import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

fun View.visibleOrGone(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.visibleOrInvisible(visible: Boolean){
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

fun View.enable(enable: Boolean){
    this.isEnabled = enable
}

fun EditText.getString(): String = this.text.toString()

fun EditText.showError(msg: String) {
    when (val parent = this.parent.parent) {
        is TextInputLayout -> parent.showError(msg)
        else -> this.error = msg
    }
}

fun EditText.hideError() {
    when (val parent = this.parent.parent) {
        is TextInputLayout -> parent.hideError()
        else -> this.error = null
    }
}

fun TextInputLayout.showError(msg: String) {
    this.isErrorEnabled = true
    this.error = msg
}

fun TextInputLayout.hideError() {
    this.isErrorEnabled = false
}