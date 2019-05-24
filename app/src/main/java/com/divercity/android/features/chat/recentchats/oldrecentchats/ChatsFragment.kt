package com.divercity.android.features.chat.recentchats.oldrecentchats

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.bus.RxBus
import com.divercity.android.core.bus.RxEvent
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.features.chat.recentchats.newrecentchats.RecentChatViewHolder
import io.reactivex.disposables.Disposable
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

    private var isListRefreshing = true
    private var isListRefreshingNoUI = false

    private var handlerSearch = Handler()
    private var searchView: SearchView? = null
    private var searchItem: MenuItem? = null

    private var pagedList: PagedList<ExistingUsersChatListItem>? = null
    private lateinit var newMessageDisposable: Disposable

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

        newMessageDisposable = RxBus.listen(RxEvent.OnNewMessageReceived::class.java).subscribe {
            isListRefreshingNoUI = true
            viewModel.refresh()
        }

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
        list.itemAnimator = null
        list.adapter = adapter
    }

    private val listener: RecentChatViewHolder.Listener = object : RecentChatViewHolder.Listener {

        override fun onChatClick(chat: ExistingUsersChatListItem) {
            navigator.navigateToChatActivity(
                this@ChatsFragment,
                chat.name!!,
                chat.id.toString(),
                chat.chatId
            )
        }
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList.observe(viewLifecycleOwner, Observer {
            //            adapter.submitList(it)
            pagedList = it
        })

        viewModel.networkState().observe(viewLifecycleOwner, Observer {
            if (!isListRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS) {
                if ((isListRefreshing || isListRefreshingNoUI) &&
                    (it?.status == Status.SUCCESS || it?.status == Status.ERROR)
                ) {
                    isListRefreshingNoUI = false
                    isListRefreshing = false
                    swipe_list_main.isRefreshing = false
                    swipe_list_main.isEnabled = it.status == Status.SUCCESS
                    adapter.submitList(pagedList)
                }
                adapter.setNetworkState(it)
            }
        })

        viewModel.refreshState().observe(viewLifecycleOwner, Observer { networkState ->

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

                if (!isListRefreshingNoUI)
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
            isEnabled = true
            setColorSchemeColors(
                ContextCompat.getColor(context, R.color.colorPrimaryDark),
                ContextCompat.getColor(context, R.color.colorPrimary),
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
            isRefreshing = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView?.queryHint = getString(R.string.search)

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                handlerSearch.removeCallbacksAndMessages(null)
                viewModel.fetchData(viewLifecycleOwner, query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handlerSearch.removeCallbacksAndMessages(null)
                handlerSearch.postDelayed({
                    viewModel.fetchData(viewLifecycleOwner, newText)
                }, AppConstants.SEARCH_DELAY)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        if (adapter.getNetworkState()?.status == Status.SUCCESS) {
            isListRefreshingNoUI = true
            viewModel.refresh()
        }
    }

    override fun retry() {
        viewModel.retry()
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)

        searchView?.setOnQueryTextListener(null)
        searchItem?.setOnActionExpandListener(null)
        searchItem = null
        super.onDestroyView()
    }
}