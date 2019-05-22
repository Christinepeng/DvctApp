package com.divercity.android.features.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.ImageUtils
import com.divercity.android.data.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_profile_settings.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

/**
 * Created by lucas on 24/10/2018.
 */

class ProfileSettingsFragment : BaseFragment() {

    private var photoFile: File? = null
    private var isPictureSet: Boolean = false

    private lateinit var viewModel: ProfileSettingsViewModel

    companion object {

        private const val SAVE_PARAM_FILEPATH = "saveParamFilepath"

        fun newInstance(): ProfileSettingsFragment {
            return ProfileSettingsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_profile_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[ProfileSettingsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as ProfileSettingsActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.setTitle(R.string.settings)
            }
        }
    }

    private fun setupView() {
        showUserPicture()

        txt_change_profile_pic.setOnClickListener {
            EasyImage.openChooserWithGallery(this, getString(R.string.pick_source), 0)
        }

        lay_account_settings.setOnClickListener {
            navigator.navigateToAccountSettingsActivity(this)
        }

        lay_change_password.setOnClickListener {
            navigator.navigateToChangePassword(this)
        }

//        lay_personal_settings.setOnClickListener {
//            navigator.navigateToPersonalSettingsActivity(this)
//        }
//
//        lay_interests.setOnClickListener {
//            navigator.navigateToInterestsActivity(this)
//        }

        enableSwitchListener()
    }

    private fun showUserPicture(){

        GlideApp.with(this)
            .load(viewModel.getProfilePicture())
            .apply(RequestOptions().circleCrop())
            .into(img_profile)
    }

    private fun enableSwitchListener() {
        switch_notifications.setOnCheckedChangeListener { _, b ->
            viewModel.enableNotifications(b)
            switch_notifications.setOnCheckedChangeListener(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            activity,
            object : DefaultCallback() {
                override fun onImagePickerError(
                    e: Exception?,
                    source: EasyImage.ImageSource?,
                    type: Int
                ) {
                    //Some error handling
                    e!!.printStackTrace()
                    showToast(e.message)
                }

                override fun onImagesPicked(
                    imageFiles: List<File>,
                    source: EasyImage.ImageSource,
                    type: Int
                ) {
                    onPhotosReturned(imageFiles[0])
                }

                override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                    //Cancel handling, you might wanna remove taken photoFile if it was canceled
                    if (source == EasyImage.ImageSource.CAMERA_IMAGE) {
                        val photoFile = EasyImage.lastlyTakenButCanceledPhoto(requireActivity())
                        photoFile?.delete()
                    }
                }
            })
    }

    private fun onPhotosReturned(file: File?) {
        photoFile = file
        isPictureSet = true
        viewModel.uploadPicture(ImageUtils.getStringBase64(photoFile, 600, 600)!!)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SAVE_PARAM_FILEPATH, photoFile)
    }

    fun subscribeToLiveData() {
        viewModel.updateFCMTokenResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    switch_notifications.isChecked = !response.data!!
                    showSnackbar(response.message)
                    enableSwitchListener()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    enableSwitchListener()
                }
            }
        })

        viewModel.uploadProfilePictureResponse.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    showToast(it.message)
                }
                Status.SUCCESS -> {
                    hideProgress()
                    // So the picture is cached
                    showUserPicture()
                    GlideApp.with(this)
                        .load(photoFile)
                        .apply(RequestOptions().circleCrop())
                        .into(img_profile)
                }
            }
        })
    }

    fun showSnackbar(message: String?) {
        activity?.run {
            Snackbar.make(
                findViewById(android.R.id.content),
                message ?: "Error",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        EasyImage.clearConfiguration(requireActivity())
        super.onDestroy()
    }
}