package com.app.sampleapp.data.local.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.app.sampleapp.model.Credential
import javax.inject.Inject

class PreferenceProvider @Inject constructor(
    private val appContext: Context
) {

    companion object {

        private const val KEY_USER_ID = "key_user_id"
        private const val KEY_USERNAME = "key_username"
        private const val KEY_IS_LOGIN = "key_is_login"

    }

    private val preference: SharedPreferences
        get() = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun saveCredentials(credential: Credential) {
        preference.edit {
            putInt(KEY_USER_ID, credential.userId)
            putString(KEY_USERNAME, credential.username)
            putBoolean(KEY_IS_LOGIN, true)
        }
    }

    fun removeCredentials() {
        preference.edit {
            remove(KEY_USER_ID)
            remove(KEY_USERNAME)
            putBoolean(KEY_IS_LOGIN, false)
        }
    }

    fun getCredential(): Credential {
        val userId = preference.getInt(KEY_USER_ID, 0)
        val username = preference.getString(KEY_USERNAME, null) ?: ""
        return Credential(
            userId = userId,
            username = username
        )
    }

    fun getLoginState(): Boolean = preference.getBoolean(KEY_IS_LOGIN, false)

}
