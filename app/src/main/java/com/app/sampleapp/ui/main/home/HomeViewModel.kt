package com.app.sampleapp.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sampleapp.data.local.preference.PreferenceProvider
import com.app.sampleapp.data.remote.api.ApiService
import com.app.sampleapp.model.Credential
import com.app.sampleapp.model.ImageResult
import com.app.sampleapp.vo.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceProvider: PreferenceProvider
) : ViewModel() {

    private val _loginState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loginState = _loginState.asStateFlow()

    private fun getLoginState() {
        viewModelScope.launch {
            val loginState = preferenceProvider.getLoginState()
            _loginState.emit(loginState)
        }
    }

    val userCredential: SharedFlow<Credential> = _loginState.map {
        preferenceProvider.getCredential()
    }.shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)

    fun logout() {
        viewModelScope.launch {
            preferenceProvider.removeCredentials()
        }
    }

    /**
     * UI State
     */
    private val _uiState = MutableSharedFlow<UiState<ImageResult>>()
    val uiState = _uiState.asSharedFlow()
    private var sendImageJob: Job? = null

    fun setLoadingUiState() {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)
        }
    }

    fun sendImage(imageFile: File) {
        sendImageJob?.cancel()
        sendImageJob = viewModelScope.launch {
            try {

                val imageRequestBody = imageFile.asRequestBody(
                    "multipart/form-data;boundary=*****".toMediaTypeOrNull()
                )
                val requestBody = MultipartBody.Part.createFormData(
                    "file",
                    imageFile.name,
                    imageRequestBody
                )

                val response = apiService.sendImage(file = requestBody)

                if (response.success) {
                    val result = response.asDomain(imageFile)
                    _uiState.emit(UiState.Success(result))
                } else {
                    _uiState.emit(UiState.Failure(400, response.message))
                }

            } catch (e: Exception) {
                _uiState.emit(UiState.Error(e))
            }

        }
    }

    init {
        getLoginState()
    }

}