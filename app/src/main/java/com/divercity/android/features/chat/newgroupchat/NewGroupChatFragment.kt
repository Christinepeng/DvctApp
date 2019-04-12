package com.divercity.android.features.chat.newgroupchat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.features.chat.creategroupchat.CreateGroupChatFragment
import com.divercity.android.features.chat.newgroupchat.adapter.UserMultipleAdapter
import kotlinx.android.synthetic.main.fragment_new_group_chat.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class NewGroupChatFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: NewGroupChatViewModel

    @Inject
    lateinit var adapter: UserMultipleAdapter

    private var isListRefreshing = false

    private var handlerSearch = Handler()

    companion object {

        const val REQUEST_CODE_GROUP_CREATED = 300

        fun newInstance(): NewGroupChatFragment {
            return NewGroupChatFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_new_group_chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NewGroupChatViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.new_group_chat)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
        initAdapter()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun initAdapter() {
        adapter.setRetryCallback(this)
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

        btn_continue.setOnClickListener {
            if(adapter.selectedUsers.isNotEmpty()) {
                CreateGroupChatFragment.selectedUsers = adapter.selectedUsers
                navigator.navigateToCreateGroupChatActivityForResult(this, REQUEST_CODE_GROUP_CREATED)
            }
        }
    }

    private fun subscribeToLiveData() {

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList.observe(this, Observer {
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

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GROUP_CREATED){
                activity?.apply {
                    setResult(Activity.RESULT_OK, intent)
                    activity!!.finish ()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}