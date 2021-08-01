package com.app.sampleapp.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import java.io.File

fun Context.deleteFilesOnExternalCacheDir(path: String) {
    val dir = File(this.externalCacheDir, path)
    dir.listFiles()?.let { files ->
        files.forEach { file ->
            if (file.deleteRecursively()) {
                Log.d("TAG","${file.absoluteFile} delete file was called")
            } else {
                Log.d("TAG","${file.absoluteFile} there is no file")
            }
        }
    }
}


fun Context.getStringResource(@StringRes resourceId: Int): String =
    this.resources.getString(resourceId)

fun Context.getStringResource(@StringRes resourceId: Int, vararg formatArgs: Any): String =
    this.resources.getString(resourceId, formatArgs)

fun Context.getDimens(@DimenRes id: Int): Int = this.resources.getDimensionPixelOffset(id)

fun Activity.showShortToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Activity.showLongToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Fragment.showShortToast(message: String) =
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()

fun Fragment.showLongToast(message: String) =
    Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()

fun Fragment.finishActivity() = this.activity?.finish()

fun <T> Context.openActivity(
    destination: Class<T>,
    clearTask: Boolean = false,
    singleTop: Boolean = false,
    extras: Intent.() -> Unit = {}
) {
    val intent = Intent(this, destination)
    extras.invoke(intent)
    if (clearTask) intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    if (singleTop) intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
    startActivity(intent)
}

fun <T> Fragment.openActivity(
    destination: Class<T>,
    clearTask: Boolean = false,
    singleTop: Boolean = false,
    extras: Intent.() -> Unit = {}
) {
    val intent = Intent(this.context, destination)
    extras.invoke(intent)
    if (clearTask) intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    if (singleTop) intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
    startActivity(intent)
}