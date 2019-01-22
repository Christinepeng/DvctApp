package com.divercity.android.features.chat.chat

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.extension.networkInfo
import com.divercity.android.data.Status
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.chat.chat.chatadapter.ChatAdapter
import com.divercity.android.features.chat.chat.useradapter.UserMentionAdapter
import com.divercity.android.features.chat.chat.useradapter.UserMentionViewHolder
import com.divercity.android.repository.user.UserRepository
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject



/**
 * Created by lucas on 24/12/2018.
 */

class ChatFragment : BaseFragment() {

    lateinit var viewModel: ChatViewModel
    var userName: String? = null

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var adapter: ChatAdapter

    @Inject
    lateinit var userAdapter: UserMentionAdapter

    var isShowingUsers = false
    var positionOfAt = 0

    var lastPositionMention = HashMap<Int, UserResponse>()
    var isReplacing = false

    companion object {

        private const val PARAM_USER_ID = "paramUserId"
        private const val PARAM_USER_NAME = "paramUserName"
        private const val PARAM_CHAT_ID = "paramChatId"

        fun newInstance(userName: String, userId: String, chatId: Int?): ChatFragment {
            val fragment = ChatFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_USER_ID, userId)
            arguments.putString(PARAM_USER_NAME, userName)
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

        userName = arguments?.getString(PARAM_USER_NAME)
        viewModel.userId = arguments?.getString(PARAM_USER_ID)
        viewModel.chatId = arguments?.getInt(PARAM_CHAT_ID)

        viewModel.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.title = arguments?.getString(PARAM_USER_NAME)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        subscribeToLiveData()
        setupView()


        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            val token = it.token
        }
    }

    private fun setupView() {
        btn_send.setOnClickListener {
            if (et_msg.text.toString() != "")
                viewModel.sendMessage(et_msg.text.toString())
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
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val message = p0.toString()
                if (message != "" && !isReplacing) {
                    val lastCharacter = message.substring(message.length - 1)
                    val lastWord = message.substring(message.lastIndexOf(" ") + 1)
                    val lastWordTilAt = message.substringAfterLast("@")

                    if (lastWord != "" && lastWord[0] == '@' && !isShowingUsers) {
                        showList(true)
                    } else if (lastCharacter != " " && isShowingUsers) {
                        viewModel.filterUserList(lastWordTilAt)
                    } else if (lastCharacter == " ") {
                        showList(false)
                        viewModel.filterUserList("")
                    }
                } else if(isReplacing) {
                    isReplacing = false
                    showList(false)
                    viewModel.filterUserList("")
                }
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
    }


    private fun replaceMention(user: UserResponse) {
        val actualText = et_msg.text.toString()
        val newVal = actualText.replaceAfterLast("@", user.userAttributes?.name!!)
        lastPositionMention[newVal.length] = user
        isReplacing = true
        et_msg.setText(newVal)

//        val sb = SpannableStringBuilder("")
//        val bss = StyleSpan(android.graphics.Typeface.BOLD) // Span to make text bold
//        sb.setSpan(bss, 0, 10, Spannable.SPAN_INCLUSIVE_INCLUSIVE) // make first 10 characters Bold
        isShowingUsers = false
        et_msg.setText(newVal)
        et_msg.setSelection(newVal.length)
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
    }

    override fun onPause() {
        super.onPause()
        activity!!.unregisterReceiver(networkChangeReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun showList(active: Boolean) {
        if (active)
            list_users.visibility = View.VISIBLE
        else
            list_users.visibility = View.GONE
        isShowingUsers = active
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