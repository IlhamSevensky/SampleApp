package com.app.sampleapp.ui.main.home

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.sampleapp.R
import com.app.sampleapp.common.LoadingDialog
import com.app.sampleapp.databinding.FragmentHomeBinding
import com.app.sampleapp.extensions.showShortToast
import com.app.sampleapp.model.Credential
import com.app.sampleapp.model.ImageResult
import com.app.sampleapp.ui.auth.AuthActivity
import com.app.sampleapp.util.Constants
import com.app.sampleapp.util.FileUriUtils
import com.app.sampleapp.util.ImageUtil
import com.app.sampleapp.vo.UiState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private val viewModel by viewModels<HomeViewModel>()

    private val requestGetImage = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null){
            val file = copyImageToCache(uri)
            sendImage(file)
        }
    }

    private val requestTakePicture = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val file = savePictureTakenToCache(bitmap)
            sendImage(file)
        }
    }

    private fun navigateToResult(result: ImageResult) {
        val directions = HomeFragmentDirections.actionHomeToResult(
            result = result
        )
        findNavController().navigate(directions)
    }

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) openCamera()
    }

    private val requestReadStoragePermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) openGallery()
    }

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initLoadingDialog()
        initSubscribe()
        initListener()
    }

    private fun initLoadingDialog() {
        loadingDialog = LoadingDialog(requireContext(), this.layoutInflater)
    }

    private fun initSubscribe() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.loginState.collect()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.userCredential.collect { credential ->
                setData(credential)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiState.collect { state ->
                handleUiState(state)
            }
        }
    }

    private fun handleUiState(state: UiState<ImageResult>) {
        when (state) {
            is UiState.Loading -> showLoadingDialog(show = true)
            is UiState.Success -> {
                showLoadingDialog(show = false)
                navigateToResult(state.data)
            }
            is UiState.Error -> {
                showLoadingDialog(show = false)
                state.throwable.message?.let { showShortToast(it) }
            }
            is UiState.Failure -> {
                showLoadingDialog(show = false)
                showShortToast(state.message)
            }
        }
    }

    private fun sendImage(imageFile: File) {
        setLoadingUiState()
        viewLifecycleOwner.lifecycleScope.launch {
            val compressedImage = compressImage(imageFile)
            viewModel.sendImage(compressedImage)
        }
    }

    private suspend fun compressImage(
        imageFile: File
    ): File = Compressor.compress(requireContext(), imageFile, IO) {
        size(2_097_152)
    }

    private fun setLoadingUiState() {
        viewModel.setLoadingUiState()
    }

    private fun showLoadingDialog(show: Boolean) {
        loadingDialog.apply { if (show) show() else dismiss() }
    }

    private fun setData(credential: Credential) {
        binding.toolbarHome.title = "Halo, ${credential.username}"
    }

    private fun initToolbar() {
        binding.toolbarHome.setOnMenuItemClickListener { menuItem ->
            onMenuItemClickListener(menuItem)
        }
    }

    private fun checkPermission(
        context: Context,
        permission: String,
        action: (isGranted: Boolean, permission: String) -> Unit
    ) {
        val isGranted = ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED
        action.invoke(isGranted, permission)
    }

    private fun createImagePickerDialog(
        onCameraSelected: (dialog: DialogInterface) -> Unit,
        onGallerySelected: (dialog: DialogInterface) -> Unit
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MaterialAlertDialog_Rounded
        )
        builder.setTitle("Choose an action")

        val imageSource = arrayOf("Camera", "Gallery")
        builder.setItems(imageSource) { dialog, pos ->
            when (imageSource[pos].lowercase()) {
                "camera" -> onCameraSelected.invoke(dialog)
                "gallery" -> onGallerySelected.invoke(dialog)
            }
        }

        return builder.create()
    }

    private fun onMenuItemClickListener(
        menuItem: MenuItem
    ): Boolean = when (menuItem.itemId) {
        R.id.action_logout -> {
            logout()
            true
        }
        else -> false
    }

    private fun initListener() {
        binding.btnGetImage.setOnClickListener {
            showImagePickerDialog()
        }
    }

    private fun showImagePickerDialog() {
        val dialog = createImagePickerDialog(
            onCameraSelected = { dialog ->
                checkPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) { isGranted, permission ->
                    if (isGranted) {
                        openCamera()
                    } else {
                        requestCameraPermission.launch(permission)
                    }
                }
                dialog.dismiss()
            },
            onGallerySelected = { dialog ->
                checkPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) { isGranted, permission ->
                    if (isGranted) {
                        openGallery()
                    } else {
                        requestReadStoragePermission.launch(permission)
                    }
                }
                dialog.dismiss()
            }
        )

        dialog.show()
    }

    private fun logout() {
        viewModel.logout()
        AuthActivity.startActivity(requireContext(), clearTask = true)
    }

    private fun openCamera() {
        requestTakePicture.launch()
    }

    private fun openGallery() {
        val mimeTypes = arrayOf(
            Constants.MIME_TYPE_JPG,
            Constants.MIME_TYPE_JPEG,
            Constants.MIME_TYPE_PNG
        )
        requestGetImage.launch(mimeTypes)
    }

    private fun savePictureTakenToCache(
        bitmap: Bitmap
    ): File = ImageUtil.saveImageToCache(
        context = requireContext(),
        bitmap = bitmap,
        targetDir = Constants.IMAGE_CACHE_DIRECTORY
    )

    private fun copyImageToCache(
        uri: Uri
    ): File {
        val realPath = FileUriUtils.getRealPath(requireContext(), uri)
        val actualImage = File(realPath)
        return ImageUtil.copyFileToCache(
            context = requireContext(),
            file = actualImage,
            targetDir = Constants.IMAGE_CACHE_DIRECTORY
        )
    }

}
