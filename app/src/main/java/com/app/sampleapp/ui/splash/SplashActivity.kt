package com.app.sampleapp.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.sampleapp.data.local.preference.PreferenceProvider
import com.app.sampleapp.ui.auth.AuthActivity
import com.app.sampleapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var preferenceProvider: PreferenceProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isLogin = preferenceProvider.getLoginState()

        if (isLogin) {
            MainActivity.startActivity(this, clearTask = true)
        } else {
            AuthActivity.startActivity(this, clearTask = true)
        }
        finish()

    }

}