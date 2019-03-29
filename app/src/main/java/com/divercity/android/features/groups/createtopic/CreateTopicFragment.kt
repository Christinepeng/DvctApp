package com.divercity.android.features.groups.createtopic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.ImageUtils
import com.divercity.android.data.Status
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.groups.followedgroups.FollowingGroupsFragment
import kotlinx.android.synthetic.main.fragment_create_topic.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

/**
 * Created by lucas on 25/10/2018.
 */

class CreateTopicFragment : BaseFragment() {

    lateinit var viewModel: CreateTopicViewModel

    private var photoFile: File? = null
    private var currentGroup: GroupResponse? = null

    companion object {

        private const val REQUEST_CODE_GROUP = 300

        private const val PARAM_GROUP = "paramGroup"

        fun newInstance(group: GroupResponse?): CreateTopicFragment {
            val fragment = CreateTopicFragment()
            val arguments = Bundle()
            arguments.putParcelable(PARAM_GROUP, group)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_create_topic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[CreateTopicViewModel::class.java]
        currentGroup = arguments?.getParcelable(PARAM_GROUP)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.create_new_topic)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setupView() {
        showGroup(currentGroup)

        btn_add_photo.setOnClickListener {
            EasyImage.openChooserWithGallery(this, "Pick source", 0)
        }

        btn_create.setOnClickListener {
            if (checkFormIsCompleted() && currentGroup != null)
                viewModel.createNewTopic(
                    et_topic_name.text.toString(),
                    currentGroup!!,
                    ImageUtils.getStringBase64(photoFile, 600, 600)
                )
            else
                showToast("Complete field")
        }

        btn_choose_group.setOnClickListener {
            if (currentGroup != null)
                navigator.navigateToGroupDetail(this, currentGroup!!)
            else
                navigator.navigateToFollowingGroupsActivityForResult(this, REQUEST_CODE_GROUP)
        }

        btn_change_group.setOnClickListener {
            navigator.navigateToFollowingGroupsActivityForResult(this, REQUEST_CODE_GROUP)
        }
    }

    private fun checkFormIsCompleted(): Boolean {
        return et_topic_name.text.toString() != ""
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

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GROUP -> {
                    showGroup(
                        data?.extras?.getParcelable(FollowingGroupsFragment.GROUP_PICKED)
                    )
                }
            }
        }
    }

    private fun showGroup(group: GroupResponse?) {
        if (group != null) {
            btn_change_group.visibility = View.VISIBLE
            currentGroup = group
            txt_title.text = group.attributes.title

            GlideApp.with(this)
                .load(group.attributes.pictureMain)
                .apply(RequestOptions().circleCrop())
                .into(btn_choose_group)
        }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun onPhotosReturned(file: File?) {
        photoFile = file
        GlideApp.with(this)
            .load(file)
            .into(img_topic)
        lay_img.visibility = View.VISIBLE
    }

    private fun subscribeToLiveData() {
        viewModel.createTopicResponse.observe(viewLifecycleOwner, Observer { response ->
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
                    activity!!.finish()
                }
            }
        })
    }
}