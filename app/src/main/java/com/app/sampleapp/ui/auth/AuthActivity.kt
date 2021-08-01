package com.app.sampleapp.ui.auth

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.sampleapp.databinding.ActivityAuthBinding
import com.app.sampleapp.extensions.openActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {

        fun startActivity(context: Context, clearTask: Boolean) {
            context.openActivity(AuthActivity::class.java, clearTask = clearTask)
        }

    }
}