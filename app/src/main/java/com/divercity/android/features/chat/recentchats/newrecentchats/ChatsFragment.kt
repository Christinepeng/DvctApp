package com.divercity.android.features.chat.recentchats.newrecentchats

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import kotlinx.android.synthetic.main.fragment_chats.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class ChatsFragment : BaseFragment() {

    lateinit var viewModel: ChatsViewModel

    @Inject
    lateinit var adapter: ChatsAdapter

    private var isRefreshing = false

    companion object {

        fun newInstance(): ChatsFragment {
            return ChatsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_chats

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChatsViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.direct_messages)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
        setupView()
        subscribeToLiveData()
    }

    private fun setupView() {
        adapter.listener = object : RecentChatViewHolder.Listener {

            override fun onChatClick(chat: ExistingUsersChatListItem) {
                navigator.navigateToChatActivity(
                    this@ChatsFragment,
                    chat.name!!,
                    chat.id.toString(),
                    chat.chatId
                )
            }
        }

//        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
//
//            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
//                if(isRefreshing) {
//                    list.scrollToPosition(0)
//                    isRefreshing = false
//                }
//            }
//        })

        list.adapter = adapter

        btn_new_chat.setOnClickListener {
            navigator.navigateToNewChatActivity(this)
        }

        btn_create_new_chat.setOnClickListener {
            navigator.navigateToNewChatActivity(this)
        }

        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                    checkEndOffset() // Each time when list is scrolled check if end of the list is reached
            }
        })

        swipe_list_main.apply {
            setOnRefreshListener {
                isRefreshing = true
                viewModel.refresh()
            }
            setColorSchemeColors(
                ContextCompat.getColor(context, R.color.colorPrimaryDark),
                ContextCompat.getColor(context, R.color.colorPrimary),
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
        }
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
        viewModel.fetchCurrentChatsResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    swipe_list_main.isRefreshing = false
                }
                Status.SUCCESS -> {
                    swipe_list_main.isRefreshing = false
                }
            }
        })

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPagedListLiveData()
        })

        viewModel.showNoRecentMessages.observe(viewLifecycleOwner, Observer {
            if (it!!)
                lay_no_messages.visibility = View.VISIBLE
            else
                lay_no_messages.visibility = View.GONE
        })
    }

    private fun subscribeToPagedListLiveData() {
        viewModel.pagedChatsList?.removeObservers(viewLifecycleOwner)
        viewModel.pagedChatsList?.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }
}