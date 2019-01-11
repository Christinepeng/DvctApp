package com.divercity.app.features.chat.recentchats

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.divercity.app.AppConstants
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.data.Status
import com.divercity.app.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.app.features.chat.recentchats.adapter.RecentChatAdapter
import com.divercity.app.features.chat.recentchats.adapter.RecentChatViewHolder
import kotlinx.android.synthetic.main.fragment_chats.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class ChatsFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: ChatsViewModel

    @Inject
    lateinit var adapter: RecentChatAdapter

    private var isListRefreshing = false

    private var handlerSearch = Handler()
    private lateinit var searchView : SearchView
    private var searchItem: MenuItem?= null

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
        subscribeToPaginatedLiveData()
    }

    private fun setupView() {
        initAdapter()
        initSwipeToRefresh()
        btn_new_chat.setOnClickListener {
            navigator.navigateToNewChatActivity(this)
        }

        btn_create_new_chat.setOnClickListener {
            navigator.navigateToNewChatActivity(this)
        }
    }

    private fun subscribeToLiveData() {
        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun initAdapter() {
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter
    }

    private val listener : RecentChatViewHolder.Listener = object : RecentChatViewHolder.Listener{

        override fun onChatClick(chat: ExistingUsersChatListItem) {
            navigator.navigateToChatActivity(this@ChatsFragment, chat.name!!,chat.id.toString())
        }
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedChatsList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(this, Observer {
            if (!isListRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(this, Observer { networkState ->

            adapter.currentList?.let { pagedList ->
                if (networkState?.status != Status.LOADING)
                    isListRefreshing = false

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0) {
                    if (viewModel.lastSearch == null || viewModel.lastSearch == "") {
                        lay_no_messages.visibility = View.VISIBLE
                        txt_no_results.visibility = View.GONE
                    } else {
                        lay_no_messages.visibility = View.GONE
                        txt_no_results.visibility = View.VISIBLE
                    }
                } else {
                    lay_no_messages.visibility = View.GONE
                    txt_no_results.visibility = View.GONE
                }

                swipe_list_main.isRefreshing = isListRefreshing
            }

            if (!isListRefreshing)
                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })
    }

    private fun initSwipeToRefresh() {

        swipe_list_main.apply {
            setOnRefreshListener {
                isListRefreshing = true
                viewModel.refresh()
            }
            isEnabled = false
            setColorSchemeColors(
                    ContextCompat.getColor(context, R.color.colorPrimaryDark),
                    ContextCompat.getColor(context, R.color.colorPrimary),
                    ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                handlerSearch.removeCallbacksAndMessages(null)
                viewModel.fetchChats(this@ChatsFragment, query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handlerSearch.removeCallbacksAndMessages(null)
                handlerSearch.postDelayed({
                    viewModel.fetchChats(this@ChatsFragment, newText)
                }, AppConstants.SEARCH_DELAY)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun retry() {
        viewModel.retry()
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)

        searchView.setOnQueryTextListener(null)
        searchItem?.setOnActionExpandListener(null)
        searchItem = null
        super.onDestroyView()
    }
}