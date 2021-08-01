package com.app.sampleapp.data.remote.response

import com.app.sampleapp.model.ImageResult
import com.squareup.moshi.Json
import java.io.File

data class ImageResultResponse(

    val success: Boolean,

    val message: String,

    @field:Json(name = "class")
    val clazz: String

) {

    fun asDomain(image: File) = ImageResult(
        success, message, clazz, image
    )

}