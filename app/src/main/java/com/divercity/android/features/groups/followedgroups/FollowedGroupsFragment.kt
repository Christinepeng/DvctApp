package com.divercity.android.features.groups.followedgroups

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.groups.adapter.GroupsAdapter
import com.divercity.android.features.groups.adapter.GroupsViewHolder
import com.divercity.android.model.position.GroupPosition
import kotlinx.android.synthetic.main.fragment_followed_groups.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class FollowedGroupsFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: FollowedGroupsViewModel

    @Inject
    lateinit var adapter: GroupsAdapter

    private var isListRefreshing = false
    private var lastGroupPositionTap = 0

    companion object {

        const val REQUEST_CODE_GROUP = 200

        fun newInstance(): FollowedGroupsFragment {
            return FollowedGroupsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_followed_groups

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(FollowedGroupsViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter
        initSwipeToRefresh()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.your_groups)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun subscribeToLiveData() {

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList.observe(viewLifecycleOwner, Observer {
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

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
                    txt_no_results.visibility = View.VISIBLE
                else {
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

    override fun retry() {
        viewModel.retry()
    }

    private val listener = object : GroupsViewHolder.Listener {

        override fun onGroupRequestJoinClick(groupPosition: GroupPosition) {}

        override fun onGroupJoinClick(groupPosition: GroupPosition) {}

        override fun onGroupClick(position: Int, group: GroupResponse) {
            lastGroupPositionTap = position
            navigator.navigateToGroupDetailForResult(this@FollowedGroupsFragment, group, REQUEST_CODE_GROUP)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GROUP) {
            adapter.notifyItemChanged(lastGroupPositionTap)
        }
    }
}