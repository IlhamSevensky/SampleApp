package com.app.sampleapp.data.remote.api

import com.app.sampleapp.data.remote.response.ImageResultResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("/predict/")
    suspend fun sendImage(
        @Part file: MultipartBody.Part
    ) : ImageResultResponse

}