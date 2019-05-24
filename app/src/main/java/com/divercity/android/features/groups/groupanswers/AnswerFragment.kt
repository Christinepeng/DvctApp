package com.divercity.android.features.groups.groupanswers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.extension.networkInfo
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.ImageUtils
import com.divercity.android.core.utils.Util
import com.divercity.android.data.Status
import com.divercity.android.features.groups.groupanswers.answeradapter.AnswerAdapter
import com.divercity.android.features.groups.groupanswers.answeradapter.AnswerViewHolder
import com.divercity.android.model.Question
import com.linkedin.android.spyglass.suggestions.SuggestionsResult
import kotlinx.android.synthetic.main.fragment_answers.*
import kotlinx.android.synthetic.main.view_image_btn_full.view.*
import kotlinx.android.synthetic.main.view_image_btn_small.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class AnswerFragment : BaseFragment() {

    lateinit var viewModel: AnswerViewModel

    @Inject
    lateinit var adapter: AnswerAdapter

    @Inject
    lateinit var userMentionWrapper: UserMentionWrapper

    //    var question: Question? = null
    private var photoFile: File? = null

    companion object {

        private const val PARAM_QUESTION = "paramQuestion"
        private const val PARAM_QUESTION_ID = "paramQuestionId"

        fun newInstance(questionId: String?, question: Question?): AnswerFragment {
            val fragment = AnswerFragment()
            val arguments = Bundle()
            arguments.putParcelable(PARAM_QUESTION, question)
            arguments.putString(PARAM_QUESTION_ID, questionId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_answers

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AnswerViewModel::class.java)

        val question = arguments?.getParcelable<Question>(PARAM_QUESTION)
        if (question != null) {
            viewModel.questionLiveData.postValue(question)
            viewModel.start(question.id!!)
        } else {
            viewModel.start(arguments?.getString(PARAM_QUESTION_ID)!!)
            viewModel.fetchQuestionById()
        }

        subscribeToLiveData()
        setupView()
    }

    private fun setupView() {
        showProgressNoBk()

        userMentionWrapper.setEditTextTokenize(et_msg)
        userMentionWrapper.mentionsEdTxt = et_msg
        userMentionWrapper.listUsers = list_users
        userMentionWrapper.onListHiding = {
            viewModel.disposeGroupMemberUseCase()
        }

        lay_image.btn_remove_img.setOnClickListener {
            photoFile = null
            lay_image.visibility = View.GONE
        }

        btn_add_image.setOnClickListener {
            EasyImage.openChooserWithGallery(this, getString(R.string.pick_source), 0)
        }

        btn_send.setOnClickListener {
            if (et_msg.text.toString() != "" || photoFile != null) {
                viewModel.sendNewAnswer(
                    userMentionWrapper.getMessageWithMentions(),
                    ImageUtils.getStringBase64(photoFile, 600, 600)
                )
            }
        }

        lay_image_full_screen.btn_close_full_screen_image.setOnClickListener {
            lay_image_full_screen.visibility = View.GONE
        }

        adapter.answerListener = object : AnswerViewHolder.Listener {

            override fun onNavigateToUserProfile(userId: String) {
                navigator.navigateToOtherUserProfile(this@AnswerFragment, userId)
            }

            override fun onImageTap(imageUrl: String) {
                lay_image_full_screen.visibility = View.VISIBLE
                GlideApp.with(this@AnswerFragment)
                    .load(imageUrl)
                    .into(lay_image_full_screen.img_full_screen)
            }
        }

        list.adapter = adapter
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (checkIfHasToScrollDown() && positionStart == 0) {
                    list.scrollToPosition(0)
                }
            }
        })


        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0)
                    checkEndOffset() // Each time when list is scrolled check if end of the list is reached
            }
        })
    }

    private fun showData(question: Question?) {
        if (question != null) {

            (activity as AppCompatActivity).apply {
                setSupportActionBar(include_toolbar.toolbar)
                supportActionBar?.let {
                    it.title = question.groupTitle
                    it.setDisplayHomeAsUpEnabled(true)
                }
            }

            userMentionWrapper.fetchUsers = { searchQuery, queryToken ->
                viewModel.fetchGroupMembers(
                    question.groupId.toString(),
                    0,
                    10,
                    searchQuery,
                    queryToken
                )
            }

            KeyboardVisibilityEvent.setEventListener(requireActivity()) {
                if (it) {
                    group_header.visibility = View.GONE
                    item_quest_cardview_pic_main.visibility = View.GONE
                } else {
                    group_header.visibility = View.VISIBLE
                    if (question.checkedPicture() != null) {
                        item_quest_cardview_pic_main.visibility = View.VISIBLE
                    }
                }
            }

            Glide
                .with(this)
                .load(question.authorProfilePicUrl)
                .apply(RequestOptions().circleCrop())
                .into(img_user)

            txt_name.text = question.authorName

            img_user.setOnClickListener {
                navigator.navigateToOtherUserProfile(this@AnswerFragment, question.authorId)
            }

            txt_name.setOnClickListener {
                navigator.navigateToOtherUserProfile(this@AnswerFragment, question.authorId)
            }

            txt_date.text = Util.getTimeAgoWithStringServerDate(question.createdAt)

            if (question.checkedPicture() != null) {
                Glide
                    .with(this)
                    .load(question.checkedPicture())
                    .into(item_quest_img_main)

                item_quest_img_main.setOnClickListener {
                    lay_image_full_screen.visibility = View.VISIBLE
                    GlideApp.with(this)
                        .load(question.checkedPicture())
                        .into(lay_image_full_screen.img_full_screen)
                }
            } else {
                item_quest_cardview_pic_main.visibility = View.GONE
            }

            txt_question.text = question.question
        }
    }

    private fun checkIfHasToScrollDown(): Boolean {
        val firstVisibleItemPosition: Int
        if (list.layoutManager is LinearLayoutManager) {
            firstVisibleItemPosition =
                (list.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        } else {
            throw IllegalStateException("LayoutManager needs to subclass LinearLayoutManager or StaggeredGridLayoutManager")
        }

        return firstVisibleItemPosition < 10
    }

    internal fun checkEndOffset() {
        val visibleItemCount = list.childCount
        val totalItemCount = list.layoutManager!!.itemCount

        val firstVisibleItemPosition: Int
        if (list.layoutManager is LinearLayoutManager) {
            firstVisibleItemPosition =
                (list.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        } else {
            throw IllegalStateException("LayoutManager needs to subclass LinearLayoutManager or StaggeredGridLayoutManager")
        }

//        Timber.e("totalItemCount: " + totalItemCount + " - visibleItemCount: " + visibleItemCount + " - firstVisibleItemPosition: " + firstVisibleItemPosition)
        viewModel.checkIfFetchMoreData(visibleItemCount, totalItemCount, firstVisibleItemPosition)
    }

    private fun subscribeToLiveData() {
        viewModel.sendNewAnswerResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    btn_send.visibility = View.GONE
                    pb_sending_msg.visibility = View.VISIBLE
                }

                Status.ERROR -> {
                    btn_send.visibility = View.VISIBLE
                    pb_sending_msg.visibility = View.GONE

                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    btn_send.visibility = View.VISIBLE
                    pb_sending_msg.visibility = View.GONE
                    photoFile = null
                    lay_image.visibility = View.GONE
                    et_msg.setText("")
                }
            }
        })

        viewModel.fetchAnswersResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    if (response.data!!.page == 0)
                        Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    if (response.data!!.answers.isEmpty() && response.data.page == 0) {
                        hideProgressNoBk()
                        txt_first_comment.visibility = View.VISIBLE
                    }
                }
            }
        })

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPagedListLiveData()
        })

        viewModel.fetchGroupMembersResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    val result = SuggestionsResult(response.data?.queryToken, response.data?.users)
                    userMentionWrapper.onReceiveSuggestionsResult(result, UserMentionWrapper.BUCKET)
                }
            }
        })

        viewModel.fetchQuestionByIdResponse.observe(viewLifecycleOwner, Observer { response ->
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
                }
            }
        })

        viewModel.questionLiveData.observe(viewLifecycleOwner, Observer {
            showData(it)
        })
    }

    private fun subscribeToPagedListLiveData() {
        viewModel.pagedListLiveData!!.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                txt_first_comment.visibility = View.GONE
                hideProgressNoBk()
            }
            adapter.submitList(it)
        })
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        requireActivity().registerReceiver(networkChangeReceiver, intentFilter)
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(networkChangeReceiver)
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        userMentionWrapper.onDestroy()
        viewModel.onDestroy()
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

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun onPhotosReturned(file: File?) {
        lay_image.visibility = View.VISIBLE
        photoFile = file
        GlideApp.with(this)
            .load(file)
            .into(lay_image.img_added)
    }

    private val networkChangeReceiver = object : BroadcastReceiver() {

        override fun onReceive(p0: Context?, p1: Intent?) {
            val netInfo = context!!.networkInfo
            if (netInfo != null && netInfo.isConnected) {
                viewModel.checkErrorsToReconnect()
            }
        }
    }
}