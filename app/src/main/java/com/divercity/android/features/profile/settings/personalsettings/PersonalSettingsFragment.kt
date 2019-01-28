package com.divercity.android.features.profile.settings.personalsettings

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.CustomTwoBtnDialogFragment
import kotlinx.android.synthetic.main.fragment_profile_settings.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

/**
 * Created by lucas on 24/10/2018.
 */

class PersonalSettingsFragment : BaseFragment() {

    val SAVE_PARAM_FILEPATH = "saveParamFilepath"
    val SAVE_PARAM_USERNAME = "username"
    val SAVE_PARAM_ISUSERREGISTERED = "isUserRegistered"

    private var photoFile: File? = null
    private var isPictureSet: Boolean = false

    private lateinit var viewModel: PersonalSettingsViewModel
    private var username: String? = ""
    private var isUserRegistered: Int = 2

    companion object {

        fun newInstance(): PersonalSettingsFragment {
            return PersonalSettingsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_personal_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[PersonalSettingsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as PersonalSettingsActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.setTitle(R.string.settings)
            }
        }
    }

    private fun setupView(){
        GlideApp.with(this)
            .load(viewModel.getProfilePicture())
            .apply(RequestOptions().circleCrop())
            .into(img_profile)

        txt_change_profile_pic.setOnClickListener {
            EasyImage.openChooserWithGallery(this, getString(R.string.pick_source), 0)
        }

        enableSwitchListener()
    }

    private fun enableSwitchListener(){
        switch_notifications.setOnCheckedChangeListener { _, b ->
            viewModel.enableNotifications(b)
            switch_notifications.setOnCheckedChangeListener(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {
            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                //Some error handling
                e!!.printStackTrace()
                showToast(e.message)
            }

            override fun onImagesPicked(imageFiles: List<File>, source: EasyImage.ImageSource, type: Int) {
                onPhotosReturned(imageFiles[0])
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                //Cancel handling, you might wanna remove taken photoFile if it was canceled
                if (source == EasyImage.ImageSource.CAMERA_IMAGE) {
                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(activity!!)
                    photoFile?.delete()
                }
            }
        })
    }

    private fun onPhotosReturned(file: File?) {
        photoFile = file
        GlideApp.with(this)
            .load(file)
            .apply(RequestOptions().circleCrop())
            .into(img_profile)
        isPictureSet = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SAVE_PARAM_FILEPATH, photoFile)
        outState.putString(SAVE_PARAM_USERNAME, username)
        outState.putInt(SAVE_PARAM_ISUSERREGISTERED, isUserRegistered)
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

    private fun setupEvents() {
//        btn_photo.setOnClickListener {
//            EasyImage.openChooserWithGallery(this, "Pick source", 0)
//        }
//
//        et_username.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
//                /* This is because when onSaveInstanceState is not null you need to restore the state
//                of EditText, if count != 1 or 0, it means that is restoring from onSaveInstanceState
//               */
//                if (count == 1 || count == 0)
//                    removeUsernameStyleAndHideImg()
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                if (!s.toString().startsWith("@") && s.toString().length == 1) {
//                    et_username.setText("@".plus(s.toString()))
//                    Selection.setSelection(et_username.text, et_username.length())
//                }
//            }
//        })
//
//        et_username.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
//            var text = et_username.text.toString()
//            if (!hasFocus && text != username && text != "" && text.substring(1) != "") {
//                viewModel.checkUsernameRegistered(text.substring(1))
//                username = et_username.text.toString()
//            }
//        }
//
//        lay_warning_username.setOnClickListener {
//            lay_warning_username.visibility = View.GONE
//        }
//
//        btn_create_account.setOnClickListener {
//            if (checkFormIsCompleted())
//                viewModel.signUp(
//                    getTextEditText(et_name),
//                    getTextEditText(et_username).substring(1),
//                    getTextEditText(et_email),
//                    getTextEditText(et_password),
//                    getTextEditText(et_confirm_pass)
//                )
//            else
//                showToast(getString(R.string.check_fields))
//        }
    }

    private fun checkFormIsCompleted(): Boolean {
//        return et_name.text.toString() != "" &&
//                et_username.text.toString() != "" &&
//                et_username.text.toString().length != 1 &&
//                et_email.text.toString() != "" &&
//                Util.isValidEmail(et_email.text.toString()) &&
//                et_password.text.toString() != "" &&
//                et_confirm_pass.text.toString() != ""
        return true
    }

    private fun getTextEditText(editText: EditText): String {
        return editText.text.toString()
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun isUsernameRegistered(b: Boolean) {
//        if (b) {
//            et_username.setTextColor(ContextCompat.getColor(activity!!, R.color.red))
//            img_username_status.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.icon_wrong))
//            img_username_status.visibility = View.VISIBLE
//            lay_warning_username.visibility = View.VISIBLE
//            isUserRegistered = 1
//        } else {
//            et_username.setTextColor(ContextCompat.getColor(activity!!, R.color.green))
//            img_username_status.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.img_checkmark))
//            img_username_status.visibility = View.VISIBLE
//            lay_warning_username.visibility = View.GONE
//            isUserRegistered = 0
//        }
    }

    private fun removeUsernameStyleAndHideImg() {
//        isUserRegistered = 2
//        et_username.setTextColor(ContextCompat.getColor(activity!!, R.color.appText1))
//        img_username_status.visibility = View.GONE
//        lay_warning_username.visibility = View.GONE
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


    override fun onDestroy() {
        EasyImage.clearConfiguration(activity!!)
        super.onDestroy()
    }
}