package com.app.sampleapp.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sampleapp.data.local.preference.PreferenceProvider
import com.app.sampleapp.model.Credential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val preferenceProvider: PreferenceProvider
) : ViewModel() {

    fun saveCredential(credential: Credential) {
        viewModelScope.launch {
            preferenceProvider.saveCredentials(credential)
        }
    }

}