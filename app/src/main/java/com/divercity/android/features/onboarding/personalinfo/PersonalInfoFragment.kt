package com.divercity.android.features.onboarding.personalinfo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.ImageUtils
import com.divercity.android.data.Status
import com.divercity.android.data.entity.profile.profile.UserProfileEntity
import com.divercity.android.features.dialogs.AddProfilePictureDialogFragment
import com.divercity.android.features.dialogs.CustomTwoBtnDialogFragment
import com.divercity.android.features.onboarding.selectusertype.SelectUserTypeViewModel
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up_personal_info.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

class PersonalInfoFragment : BaseFragment() {

    val SAVE_PARAM_FILEPATH = "saveParamFilepath"

    private var photoFile: File? = null
    private var isPictureSet: Boolean = false
    private var viewModel: PersonalInfoViewModel? = null
    override fun layoutId(): Int {
        return R.layout.fragment_sign_up_personal_info
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(
            PersonalInfoViewModel::class.java
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            val data = savedInstanceState.getSerializable(SAVE_PARAM_FILEPATH)
            if (data != null) {
                photoFile = data as File
                onPhotosReturned(photoFile)
            }
        }

        val userGender = viewModel?.sessionRepository?.getGender()
        val userEthnicityName = viewModel?.sessionRepository?.getEthnicityName()
        val userAge = viewModel?.sessionRepository?.getAgeRange()
        val userLocation = viewModel?.sessionRepository?.getLocation()

        if (userGender != null) {
            txt_user_gender.setText(userGender)
            txt_user_gender.setTextColor(Color.parseColor("#333241"))
        }
        if (userEthnicityName != null) {
            txt_user_ethnicity.setText(userEthnicityName)
            txt_user_ethnicity.setTextColor(Color.parseColor("#333241"))
        }
        if (userAge != null) {
            txt_user_age.setText(userAge)
            txt_user_age.setTextColor(Color.parseColor("#333241"))
        }
        if (userLocation != "null, null") {
            txt_user_location.setText(userLocation)
            txt_user_location.setTextColor(Color.parseColor("#333241"))
        }

        setupEvents()
        subscribeToLiveData()
    }

    private fun setupEvents() {
        btn_previous_page.setOnClickListener{
            navigator.navigateToSelectUserTypeActivity(requireActivity())
        }

        btn_next.setOnClickListener{
            navigator.navigateToProfessionalInfoActivity(requireActivity())
        }

        btn_upload_profile_picture.setOnClickListener{
            EasyImage.openChooserWithGallery(this, getString(R.string.pick_source), 0)
        }

        btn_choose_gender.setOnClickListener{
            navigator.navigateToOnboardingGenderActivity(requireActivity())
        }

        btn_choose_ethnicity.setOnClickListener{
            navigator.navigateToOnboardingEthnicityActivity(requireActivity())
        }

        btn_choose_age.setOnClickListener{
            navigator.navigateToSelectBirthdayActivity(requireActivity())
        }

        btn_choose_location.setOnClickListener{
            navigator.navigateToOnboardingLocationActivity(requireActivity())
        }

        btn_skip.setOnClickListener{
            navigator.navigateToProfessionalInfoActivity(requireActivity())
        }
    }

    private fun subscribeToLiveData() {
        viewModel?.uploadProfilePictureResponse?.observe(viewLifecycleOwner, Observer {
            when (it?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    showToast(it.message)
                }
                Status.SUCCESS -> {
                }
            }
        })
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

    companion object {
        fun newInstance(): PersonalInfoFragment {
            return PersonalInfoFragment()
        }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    //    TODO hanlde if photo uploading fails
    private fun showDialogErrorProfilePictureUpload() {
        var dialog = CustomTwoBtnDialogFragment.newInstance(
            getString(R.string.ups),
            getString(R.string.error_picture_upload),
            getString(R.string.ok),
            getString(R.string.retry)
        )

        dialog.setListener(object : CustomTwoBtnDialogFragment.OnBtnListener {

            override fun onNegativeBtnClick() {
            }

            override fun onPositiveBtnClick() {
//                viewModelJobs.uploadPicture()
            }
        })
    }

    private fun onPhotosReturned(file: File?) {
        photoFile = file
        GlideApp.with(this)
            .load(file)
            .apply(RequestOptions().circleCrop())
            .into(img_uploaded_profile_picture)
        isPictureSet = true
        viewModel?.uploadPicture(ImageUtils.getStringBase64(photoFile, 600, 600)!!)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SAVE_PARAM_FILEPATH, photoFile)
    }

    override fun onDestroy() {
        EasyImage.clearConfiguration(requireActivity())
        super.onDestroy()
    }
}