package com.divercity.android.features.chat.creategroupchat

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.Status
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.chat.creategroupchat.adapter.UserGroupMemberAdapter
import kotlinx.android.synthetic.main.fragment_create_group_chat.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class CreateGroupChatFragment : BaseFragment() {

    lateinit var viewModel: CreateGroupChatViewModel

    @Inject
    lateinit var adapter: UserGroupMemberAdapter

    companion object {

        const val SAVE_PARAM_FILEPATH = "saveParamFilepath"

        var selectedUsers: HashSet<UserResponse>? = null

        fun newInstance(): CreateGroupChatFragment {
            return CreateGroupChatFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_create_group_chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CreateGroupChatViewModel::class.java)
        setHasOptionsMenu(true)

        savedInstanceState?.let {
            val data = savedInstanceState.getSerializable(SAVE_PARAM_FILEPATH)
            if (data != null)
                onPhotosReturned(data as File)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToLiveData()

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.new_group_chat)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        adapter.setData(selectedUsers!!)
        list_members.adapter = adapter

        img_group.setOnClickListener {
            EasyImage.openChooserWithGallery(this, getString(R.string.pick_source), 0)
        }

        btn_create.setOnClickListener {
            if (et_name.text.isNullOrEmpty())
                showToast("Please write the name of the group")
            else if (adapter.selectedUsers.isEmpty())
                showToast("Please select members")
            else {
                viewModel.createGroup(adapter.selectedUsers, et_name.text.toString())
            }
        }
    }

    private fun subscribeToLiveData() {

        viewModel.createGroupChatResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {

                }
            }
        })

        viewModel.addChatGroupMemberResponse.observe(viewLifecycleOwner, Observer { response ->
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
                    navigator.navigateToChatActivity(
                        this,
                        response.data?.attributes?.name!!,
                        response.data.attributes.users!![0].id!!,
                        response.data.id.toInt()
                    )

                    activity?.apply {
                        setResult(Activity.RESULT_OK, intent)
                        activity!!.finish ()
                    }
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
        viewModel.photoFile = file
        GlideApp.with(this)
            .load(file)
            .apply(RequestOptions().circleCrop())
            .into(img_group)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SAVE_PARAM_FILEPATH, viewModel.photoFile)
    }

    override fun onDestroy() {
        EasyImage.clearConfiguration(activity!!)
        super.onDestroy()
    }
}