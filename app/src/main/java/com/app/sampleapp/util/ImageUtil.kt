package com.app.sampleapp.util

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.util.Log
import okio.FileNotFoundException
import okio.IOException
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object ImageUtil {

    fun copyFileToCache(
        context: Context,
        file: File,
        targetDir: String
    ): File {
        val contextWrapper = ContextWrapper(context)
        // get cache directory
        val cacheDir = File(contextWrapper.externalCacheDir, targetDir)

        // create time stamp
        val timeStamp: String = SimpleDateFormat(
            "ddMMyyyyHHmm",
            Locale.getDefault()
        ).format(Date())

        // if cache directory not exists, create it
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }

        val targetFile = File(cacheDir, "${timeStamp}.${file.extension}")

        try {

            file.copyTo(targetFile, overwrite = true)

        } catch (e: FileAlreadyExistsException) {
            Log.d("ERROR", "failed to copy caused by ${e.message}")
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return targetFile
    }

    fun saveImageToCache(
        context: Context,
        bitmap: Bitmap,
        targetDir: String
    ): File {
        val contextWrapper = ContextWrapper(context)
        val cacheDir = File(contextWrapper.externalCacheDir, targetDir)

        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }

        val timeStamp: String = SimpleDateFormat(
            "ddMMyyyyHHmm",
            Locale.getDefault()
        ).format(Date())

        val imageFile = File(cacheDir, "${timeStamp}.jpg")
        if (!imageFile.exists()) {
            try {
                // set image as JPEG
                imageFile.outputStream().use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                }

            } catch (e: FileNotFoundException) {
                Log.d(
                    "ERROR",
                    "Failed to save ${imageFile.absoluteFile} caused by parent folder is not exists"
                )
                e.printStackTrace()
            } catch (e: IOException) {
                Log.d("ERROR", "Failed to save ${imageFile.absoluteFile} caused by ${e.message}")
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        return imageFile
    }

}