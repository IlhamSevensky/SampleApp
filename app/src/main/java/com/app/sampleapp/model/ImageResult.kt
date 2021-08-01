package com.app.sampleapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class ImageResult(

    val success: Boolean,

    val message: String,

    val clazz: String,

    val image: File

): Parcelable {

    fun getResult(): String = "Kelas : $clazz"

}