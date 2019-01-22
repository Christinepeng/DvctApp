package com.divercity.android.features.groups.creategroup.step1

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import kotlinx.android.synthetic.main.fragment_create_group.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

/**
 * Created by lucas on 25/10/2018.
 */

class CreateGroupFragment : BaseFragment() {

    lateinit var viewModel: CreateGroupViewModel

    private var photoFile: File? = null
    private var isPictureSet: Boolean = false

    companion object {

        fun newInstance(): CreateGroupFragment {
            return CreateGroupFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_create_group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[CreateGroupViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.create_group)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setupView() {
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
                navigator.navigateToGroupDescriptionActivity(
                    this,
                    et_group_name.text.toString(),
                    photoFile?.absolutePath
                )
            }
        } else
            btn_next.setTextColor(
                ContextCompat.getColor(
                    activity!!,
                    R.color.whiteDisable
                )
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

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun onPhotosReturned(file: File?) {
        lay_photo_img.visibility = View.GONE
        photoFile = file
        GlideApp.with(this)
            .load(file)
            .into(img_default_group)
        isPictureSet = true
    }
}