package com.app.sampleapp.ui.auth.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.sampleapp.R
import com.app.sampleapp.databinding.FragmentRegisterBinding
import com.app.sampleapp.extensions.getString
import com.app.sampleapp.model.Credential
import com.app.sampleapp.ui.main.MainActivity
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)

    private val viewModel by viewModels<RegisterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()

    }

    private fun initListener() {
        binding.btnRegister.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val username = binding.edtUsername.getString()
//        val password = binding.edtPassword.getString()

        saveCredentials(userId = 1, username = username)
        navigateToHome()
    }

    private fun saveCredentials(userId: Int, username: String) {
        val credential = Credential(
            userId = userId,
            username = username
        )
        viewModel.saveCredential(credential)
    }

    private fun navigateToHome() {
        MainActivity.startActivity(requireContext(), clearTask = true)
    }

}