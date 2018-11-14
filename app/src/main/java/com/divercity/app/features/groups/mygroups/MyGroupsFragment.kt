package com.divercity.app.features.groups.mygroups

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.data.Status
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.features.groups.ITabsGroups
import com.divercity.app.features.groups.adapter.GroupsAdapter
import com.divercity.app.features.groups.adapter.GroupsViewHolder
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class MyGroupsFragment : BaseFragment(), RetryCallback, ITabsGroups {

    lateinit var viewModel: MyGroupsViewModel

    @Inject
    lateinit var adapter: GroupsAdapter

    private var positionJoinClicked: Int = 0

    companion object {

        fun newInstance(): MyGroupsFragment {
            return MyGroupsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_list_refresh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)[MyGroupsViewModel::class.java]
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter
        subscribeToPaginatedLiveData()
        subscribeToJoinLiveData()
    }

    private fun subscribeToJoinLiveData() {
        viewModel.joinGroupResponse.observe(this, Observer { school ->
            when (school?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, school.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    // Updating join btn state
                    adapter.currentList?.get(positionJoinClicked)?.attributes?.isIsFollowedByCurrent = true
                    adapter.notifyItemChanged(positionJoinClicked)
                }
            }
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedGroupList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(this, Observer {
            adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(this, Observer { networkState ->
            networkState?.let {
                adapter.currentList?.let {
                    if (networkState.status == Status.SUCCESS && it.size == 0)
                        txt_no_results.visibility = View.VISIBLE
                    else
                        txt_no_results.visibility = View.GONE
                }
            }
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    private val listener = object : GroupsViewHolder.Listener {

        override fun onGroupJoinClick(position: Int, group: GroupResponse) {
            positionJoinClicked = position
            viewModel.joinGroup(group)
        }
    }

    override fun fetchGroups(searchQuery: String) {
        viewModel.fetchGroups(searchQuery)
        subscribeToPaginatedLiveData()
    }
}