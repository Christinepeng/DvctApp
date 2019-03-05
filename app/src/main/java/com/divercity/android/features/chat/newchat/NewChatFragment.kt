package com.divercity.android.features.chat.newchat

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.chat.newchat.adapter.UserSortedByFirstCharAdapter
import com.divercity.android.features.profile.tabconnections.adapter.UserViewHolder
import kotlinx.android.synthetic.main.fragment_new_chat.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class NewChatFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: NewChatViewModel

    @Inject
    lateinit var adapter: UserSortedByFirstCharAdapter

    private var isListRefreshing = false

    private var handlerSearch = Handler()

    companion object {

        const val REQUEST_CODE_GROUP_CREATED = 300

        fun newInstance(): NewChatFragment {
            return NewChatFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_new_chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NewChatViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.new_direct_messages)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        lay_new_group.setOnClickListener {
            navigator.navigateToNewGroupChatActivityForResult(this, REQUEST_CODE_GROUP_CREATED)
        }

        initAdapter()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun initAdapter() {
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter

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

    private fun subscribeToLiveData() {

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedUserList.observe(this, Observer {
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

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
                    txt_no_results.visibility = View.VISIBLE
                else
                    txt_no_results.visibility = View.GONE

                swipe_list_main.isRefreshing = isListRefreshing
            }

            if (!isListRefreshing)
                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                handlerSearch.removeCallbacksAndMessages(null)
                viewModel.fetchUsers(viewLifecycleOwner, query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handlerSearch.removeCallbacksAndMessages(null)
                handlerSearch.postDelayed({
                    viewModel.fetchUsers(viewLifecycleOwner, newText)
                }, AppConstants.SEARCH_DELAY)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    private val listener: UserViewHolder.Listener = object : UserViewHolder.Listener {
        override fun onConnectUser(user: UserResponse) {
        }

        override fun onUserDirectMessage(user: UserResponse) {
        }

        override fun onUserClick(user: UserResponse) {
            navigator.navigateToChatActivity(this@NewChatFragment, user.userAttributes?.name!!, user.id, -1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GROUP_CREATED){
                activity?.finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}