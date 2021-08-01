package com.app.sampleapp.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.sampleapp.databinding.ActivityMainBinding
import com.app.sampleapp.extensions.openActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {

        fun startActivity(context: Context, clearTask: Boolean) {
            context.openActivity(MainActivity::class.java, clearTask = clearTask)
        }

    }
}