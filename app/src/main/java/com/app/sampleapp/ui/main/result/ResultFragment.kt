package com.app.sampleapp.ui.main.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.app.sampleapp.R
import com.app.sampleapp.databinding.FragmentResultBinding
import com.app.sampleapp.extensions.deleteFilesOnExternalCacheDir
import com.app.sampleapp.model.ImageResult
import com.app.sampleapp.util.Constants
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment(R.layout.fragment_result) {

    private val binding by viewBinding(FragmentResultBinding::bind)

    private val args: ResultFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        setData(args.result)

    }

    private fun setData(result: ImageResult) {
        binding.ivImage.load(result.image)
        binding.tvResult.text = result.getResult()
    }

    private fun initToolbar() {
        binding.toolbarResult.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().deleteFilesOnExternalCacheDir(Constants.IMAGE_CACHE_DIRECTORY)
    }

}