package com.divercity.android.features.groups.createeditgroup.step1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.Status
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.dialogs.CustomTwoBtnDialogFragment
import kotlinx.android.synthetic.main.fragment_create_group.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

/**
 * Created by lucas on 25/10/2018.
 */

class CreateEditGroupStep1Fragment : BaseFragment() {

    private var photoFile: File? = null
    private var group: GroupResponse? = null

    lateinit var viewModel: CreateEditGroupStep1ViewModel

    companion object {

        private const val PARAM_GROUP = "paramGroup"

        fun newInstance(group: GroupResponse?): CreateEditGroupStep1Fragment {
            val fragment = CreateEditGroupStep1Fragment()
            val arguments = Bundle()
            arguments.putParcelable(PARAM_GROUP, group)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_create_group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        group = arguments?.getParcelable(PARAM_GROUP)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[CreateEditGroupStep1ViewModel::class.java]

        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                if (group != null)
                    it.setTitle(R.string.edit_group)
                else
                    it.setTitle(R.string.create_group)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setupView() {
        if (group != null) {
            btn_delete_group.visibility = View.VISIBLE
            btn_change_photo.visibility = View.VISIBLE
            btn_change_photo.setOnClickListener {
                EasyImage.openChooserWithGallery(this, "Pick source", 0)
            }
            btn_delete_group.setOnClickListener {
                showDeleteWarning()
            }
            et_group_name.setText(group?.attributes?.title)
            setPicture()
        } else {
            btn_delete_group.visibility = View.GONE
            btn_change_photo.visibility = View.GONE
        }

        checkFormIsCompleted()

        img_default_group.setOnClickListener {
            EasyImage.openChooserWithGallery(this, "Pick source", 0)
        }

        et_group_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkFormIsCompleted()
            }
        })
    }

    private fun setPicture() {
        lay_photo_img.visibility = View.GONE
        img_default_group.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))
        GlideApp.with(this)
            .load(group?.attributes?.pictureMain)
            .into(img_default_group)
    }

    private fun checkFormIsCompleted() {
        if (et_group_name.text.toString() != "")
            enableSaveCreateButton(true)
        else
            enableSaveCreateButton(false)
    }

    private fun enableSaveCreateButton(boolean: Boolean) {
        btn_next.isEnabled = boolean
        if (boolean) {
            btn_next.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
            btn_next.setOnClickListener {
                navigateToNext()
            }
        } else
            btn_next.setTextColor(
                ContextCompat.getColor(
                    activity!!,
                    R.color.whiteDisable
                )
            )
    }

    private fun subscribeToLiveData() {
        viewModel.deleteGroupResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    navigator.navigateToHomeActivityAndClearTop(this)
                }
            }
        })
    }

    private fun navigateToNext() {
        if (group != null)
            navigator.navigateToEditGroupStep3(
                this,
                et_group_name.text.toString(),
                photoFile?.absolutePath,
                group
            )
        else
            navigator.navigateToCreateGroupStep3(
                this,
                et_group_name.text.toString(),
                photoFile?.absolutePath
            )
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
                        val photoFile = EasyImage.lastlyTakenButCanceledPhoto(activity!!)
                        photoFile?.delete()
                    }
                }
            })
    }

    private fun showDeleteWarning() {
        val dialog = CustomTwoBtnDialogFragment.newInstance(
            getString(R.string.delete_group),
            getString(R.string.delete_group_warning),
            getString(R.string.yes),
            getString(R.string.no)
        )

        dialog.setListener(object : CustomTwoBtnDialogFragment.OnBtnListener {

            override fun onNegativeBtnClick() {
            }

            override fun onPositiveBtnClick() {
                viewModel.deleteGroup(group!!)
            }
        })
        dialog.isCancelable = false
        dialog.show(childFragmentManager, null)
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun onPhotosReturned(file: File?) {
        lay_photo_img.visibility = View.GONE
        photoFile = file
        GlideApp.with(this)
            .load(file)
            .into(img_default_group)
    }
}