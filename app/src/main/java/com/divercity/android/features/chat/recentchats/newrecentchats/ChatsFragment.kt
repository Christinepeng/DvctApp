package com.divercity.android.features.chat.recentchats.newrecentchats

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
            if(it!!)
                lay_no_messages.visibility = View.VISIBLE
            else
                lay_no_messages.visibility = View.GONE
        })
    }

    private fun subscribeToPagedListLiveData() {
        viewModel.pagedChatsList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }
}