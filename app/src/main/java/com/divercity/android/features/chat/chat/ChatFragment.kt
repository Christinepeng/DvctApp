package com.divercity.android.features.chat.chat

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.extension.networkInfo
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.ImageUtils
import com.divercity.android.data.Status
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.chat.chat.chatadapter.ChatAdapter
import com.divercity.android.features.chat.chat.chatadapter.ChatViewHolder
import com.divercity.android.features.chat.chat.useradapter.UserMentionAdapter
import com.divercity.android.features.chat.chat.useradapter.UserMentionViewHolder
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.view_image_btn_full.view.*
import kotlinx.android.synthetic.main.view_image_btn_small.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import timber.log.Timber
import java.io.File
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class ChatFragment : BaseFragment() {

    lateinit var viewModel: ChatViewModel
    var userName: String? = null

    @Inject
    lateinit var adapter: ChatAdapter

    @Inject
    lateinit var userAdapter: UserMentionAdapter

    var isReplacing = false
    private var photoFile: File? = null

    companion object {

        private const val PARAM_USER_ID = "paramUserId"
        private const val PARAM_DISPLAY_NAME = "paramDisplayName"
        private const val PARAM_CHAT_ID = "paramChatId"

        fun newInstance(userName: String, userId: String?, chatId: Int?): ChatFragment {
            val fragment = ChatFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_USER_ID, userId)
            arguments.putString(PARAM_DISPLAY_NAME, userName)
            arguments.putInt(PARAM_CHAT_ID, chatId ?: -1)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChatViewModel::class.java)

        setHasOptionsMenu(true)

        userName = arguments?.getString(PARAM_DISPLAY_NAME)
        viewModel.userId = arguments?.getString(PARAM_USER_ID)
        viewModel.chatId = arguments?.getInt(PARAM_CHAT_ID)

        viewModel.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.title = arguments?.getString(PARAM_DISPLAY_NAME)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        subscribeToLiveData()
        setupView()
    }

    private fun setupView() {
        lay_image.btn_remove_img.setOnClickListener {
            photoFile = null
            lay_image.visibility = View.GONE
        }

        btn_add_image.setOnClickListener {
            EasyImage.openChooserWithGallery(this, getString(R.string.pick_source), 0)
        }

        btn_send.setOnClickListener {
            if (et_msg.text.toString() != "" || photoFile != null) {
                viewModel.sendMessage(et_msg.text.toString(), ImageUtils.getStringBase64(photoFile, 600, 600))
            }
        }

        adapter.chatListener = object : ChatViewHolder.Listener {

            override fun onImageTap(imageUrl: String) {
                lay_image_full_screen.visibility = View.VISIBLE
                GlideApp.with(this@ChatFragment)
                    .load(imageUrl)
                    .into(lay_image_full_screen.img_full_screen)
            }
        }

        lay_image_full_screen.btn_close_full_screen_image.setOnClickListener {
            lay_image_full_screen.visibility = View.GONE
        }

        list.adapter = adapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (checkIfHasToScrollDown() && positionStart == 0) {
                    list.scrollToPosition(0)
                }
            }
        })

        et_msg.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                val message = p0.toString()
                if (message != "" && !isReplacing) {
                    val fullText = et_msg.text.toString().substring(0, et_msg.selectionStart)
                    val lastWord = fullText.substring(fullText.lastIndexOf(" ") + 1)

                    if (lastWord != "" && lastWord[0] == '@') {
                        showList(true)
                        viewModel.filterUserList(lastWord.substring(1))
                    } else {
                        showList(false)
                    }
                } else {
                    showList(false)
                    viewModel.filterUserList("")
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!isReplacing) {
                    if (p2 > p3) {
                        val spanStyle = et_msg.text.getSpans(p1, p1 + p2, StyleSpan::class.java)
//                        val spanUser = et_msg.text.getSpans(p1, p1 + p2, ChatMember::class.java)
                        if (spanStyle.isNotEmpty()) {
                            for (i in spanStyle.indices) {
                                et_msg.text.removeSpan(spanStyle[i])
//                                et_msg.text.removeSpan(spanUser[i])
                            }
                        }
                    }
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0)
                    checkEndOffset() // Each time when list is scrolled check if end of the list is reached
            }
        })

        userAdapter.listener = object : UserMentionViewHolder.Listener {

            override fun onUserClick(data: UserResponse) {
                replaceMention(data)
            }
        }
        list_users.adapter = userAdapter

//        Paginate.with(list, object : Paginate.Callbacks{
//            override fun onLoadMore() {
//                Timber.e("onLoadMore called")
//            }
//
//            override fun isLoading(): Boolean {
//                Timber.e("isLoading called")
//                return false
//            }
//
//            override fun hasLoadedAllItems(): Boolean {
//                Timber.e("hasLoadedAllItems called")
//                return false
//            }
//
//        }).build()
    }


    private fun replaceMention(user: UserResponse) {
        isReplacing = true

        val fullTextTillCursor = et_msg.text.toString().substring(0, et_msg.selectionStart)
        val lastIndexOfAT = fullTextTillCursor.lastIndexOf("@")
        val textToInsert = "@".plus(user.userAttributes?.name!!)

        et_msg.text.replace(
            lastIndexOfAT,
            et_msg.selectionStart,
            "@".plus(user.userAttributes?.name)
        )
        viewModel.mentions.add(user)
        val bss = StyleSpan(Typeface.BOLD)
//        et_msg.text.setSpan(user.toChatMember(),
//            lastIndexOfAT,
//            lastIndexOfAT + textToInsert.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )

        et_msg.text.setSpan(
            bss,
            lastIndexOfAT,
            lastIndexOfAT + textToInsert.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        et_msg.setSelection(lastIndexOfAT + textToInsert.length)
        isReplacing = false
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
        viewModel.sendMessageResponse.observe(this, Observer { response ->
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

        viewModel.fetchChatMembersResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    userAdapter.data = response.data!!
                    userAdapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPagedListLiveData()
        })
    }

    private fun subscribeToPagedListLiveData() {
        viewModel.pagedListLiveData!!.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        activity!!.registerReceiver(networkChangeReceiver, intentFilter)
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        activity!!.unregisterReceiver(networkChangeReceiver)
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        EasyImage.clearConfiguration(context!!)
        viewModel.onDestroy()
        Timber.e("ApolloLuc onDestroy")
        adapter.onDestroy()
    }

    private fun showList(active: Boolean) {
        if (active)
            list_users.visibility = View.VISIBLE
        else
            list_users.visibility = View.GONE
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