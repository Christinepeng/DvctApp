package com.divercity.android.features.groups.selectfollowedgroup

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
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.groups.selectfollowedgroup.adapter.GroupsSimpleAdapter
import com.divercity.android.features.groups.selectfollowedgroup.adapter.GroupsSimpleViewHolder
import kotlinx.android.synthetic.main.fragment_select_followed_group.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class SelectFollowedGroupFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: SelectFollowedGroupViewModel

    @Inject
    lateinit var adapter: GroupsSimpleAdapter

    private var isListRefreshing = false

    private var handlerSearch = Handler()

    companion object {

        const val GROUP_PICKED = "groupPicked"

        fun newInstance(): SelectFollowedGroupFragment {
            return SelectFollowedGroupFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_select_followed_group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectFollowedGroupViewModel::class.java]
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        initAdapter()
        initSwipeToRefresh()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.choose_group)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun initAdapter(){
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter
    }

    private fun subscribeToLiveData() {

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(viewLifecycleOwner, Observer {
            if (!isListRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(viewLifecycleOwner, Observer { networkState ->
            adapter.currentList?.let { pagedList ->
                if (networkState?.status != Status.LOADING)
                    isListRefreshing = false

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0) {
                    if (viewModel.lastSearch == null || viewModel.lastSearch == "") {
                        lay_no_groups.visibility = View.VISIBLE
                        txt_no_results.visibility = View.GONE
                    } else {
                        lay_no_groups.visibility = View.GONE
                        txt_no_results.visibility = View.VISIBLE
                    }
                } else {
                    lay_no_groups.visibility = View.GONE
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

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null) {
                    handlerSearch.removeCallbacksAndMessages(null)
                    viewModel.fetchData(viewLifecycleOwner, query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null) {
                    handlerSearch.removeCallbacksAndMessages(null)
                    handlerSearch.postDelayed({
                        viewModel.fetchData(viewLifecycleOwner, newText)
                    }, AppConstants.SEARCH_DELAY)
                }
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun retry() {
        viewModel.retry()
    }

    private val listener = object : GroupsSimpleViewHolder.Listener {

        override fun onGroupClick(group: GroupResponse) {
            val intent = Intent()
            intent.putExtra(GROUP_PICKED, group)
            activity?.apply {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}