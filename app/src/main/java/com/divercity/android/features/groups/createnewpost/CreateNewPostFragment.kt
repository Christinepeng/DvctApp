package com.divercity.android.features.groups.createnewpost

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.ImageUtils
import com.divercity.android.data.Status
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.groups.createnewpost.adapter.GroupsSmallAdapter
import com.divercity.android.features.groups.createnewpost.adapter.selected.GroupSelectedAdapter
import com.divercity.android.features.groups.createnewpost.adapter.selected.GroupSelectedViewHolder
import kotlinx.android.synthetic.main.fragment_create_new_post.*
import kotlinx.android.synthetic.main.view_search.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class CreateNewPostFragment : BaseFragment(), RetryCallback {

    lateinit var adapter: GroupsSmallAdapter

    @Inject
    lateinit var adapterSelected: GroupSelectedAdapter

    lateinit var viewModel: CreateNewPostViewModel

    private var handlerSearch = Handler()

    var photoFile: File? = null
    private var isListRefreshing = false

    companion object {

        fun newInstance(): CreateNewPostFragment {
            return CreateNewPostFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_create_new_post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[CreateNewPostViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.create_new_post)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })

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
                    showToast("Posts created successfully")
                    requireActivity().finish()
                }
            }
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(viewLifecycleOwner, Observer {
            if (!isListRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(viewLifecycleOwner, Observer { networkState ->
            adapter.currentList?.let { pagedList ->

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0) {
                    if (viewModel.lastSearch == null || viewModel.lastSearch == "") {
                        txt_no_groups_following.visibility = View.VISIBLE
                        txt_no_results.visibility = View.GONE
                    } else {
                        txt_no_groups_following.visibility = View.GONE
                        txt_no_results.visibility = View.VISIBLE
                    }
                } else {
                    txt_no_groups_following.visibility = View.GONE
                    txt_no_results.visibility = View.GONE
                }
            }
        })
    }

    private fun setupView() {

        list_selected_groups.layoutManager = StaggeredGridLayoutManager(2, 1)
        adapterSelected.listener = object : GroupSelectedViewHolder.Listener {

            override fun onGroupRemoved(data: GroupResponse) {
                adapter.selectedGroups.remove(data)
                adapterSelected.groups?.remove(data)
                adapterSelected.notifyDataSetChanged()
                adapter.notifyDataSetChanged()
            }
        }
        list_selected_groups.adapter = adapterSelected

        adapter = GroupsSmallAdapter()
        adapter.setRetryCallback(this)
        adapter.onSelectUnselectGroup = {
            adapterSelected.groups = it
        }
        list_groups.adapter = adapter

        btn_create.setOnClickListener {
            if (isFormCompleted())
                viewModel.createNewPosts(
                    et_topic_name.text.toString().trim(), adapterSelected.getGroupsIds(),
                    ImageUtils.getStringBase64(photoFile, 600, 600)
                )
            else
                showToast("Check data")
        }

        btn_add_picture.setOnClickListener {
            EasyImage.openChooserWithGallery(this, "Pick source", 0)
        }

        btn_remove_img.setOnClickListener {
            lay_img.visibility = View.GONE
            img_topic.setImageDrawable(null)
            photoFile = null
        }

        include_search.edtxt_search.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                val toSearch: String? = include_search.edtxt_search.text.toString()

                search(toSearch)
                true
            } else
                false
        }

        include_search.edtxt_search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                search(p0.toString())
            }
        })
    }

    private fun isFormCompleted(): Boolean {
        return et_topic_name.text.toString().trim() != "" &&
                adapterSelected.getGroupsIds().isNotEmpty()
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

    private fun search(query: String?) {
        handlerSearch.removeCallbacksAndMessages(null)
        handlerSearch.postDelayed({
            viewModel.fetchData(viewLifecycleOwner, query)
        }, AppConstants.SEARCH_DELAY)
    }

    private fun showToast(msg: String?) {
        Toast.makeText(requireContext().applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    private fun onPhotosReturned(file: File?) {
        photoFile = file
        GlideApp.with(this)
            .load(file)
            .into(img_topic)
        lay_img.visibility = View.VISIBLE
    }

    override fun retry() {
        viewModel.retry()
    }
}