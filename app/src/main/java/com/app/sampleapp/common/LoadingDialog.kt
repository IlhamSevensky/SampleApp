package com.app.sampleapp.common

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.app.sampleapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoadingDialog(
    private val context: Context,
    private val inflater: LayoutInflater
){

    private var dialog: AlertDialog? = null

    @SuppressLint("InflateParams")
    fun show() {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setView(inflater.inflate(R.layout.dialog_loading, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }

}