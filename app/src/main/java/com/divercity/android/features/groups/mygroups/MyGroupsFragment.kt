package com.divercity.android.features.groups.mygroups

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.group.GroupResponse
import com.divercity.android.features.groups.ITabsGroups
import com.divercity.android.features.groups.adapter.GroupsAdapter
import com.divercity.android.features.groups.adapter.GroupsViewHolder
import kotlinx.android.synthetic.main.fragment_my_groups.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class MyGroupsFragment : BaseFragment(), RetryCallback, ITabsGroups {

    lateinit var viewModel: MyGroupsViewModel

    @Inject
    lateinit var adapter: GroupsAdapter

    private var positionJoinClicked: Int = 0
    private var isListRefreshing = false

    companion object {

        fun newInstance(): MyGroupsFragment {
            return MyGroupsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_my_groups

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
        initSwipeToRefresh()
        subscribeToPaginatedLiveData()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
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

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedGroupList.observe(this, Observer {
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
                        txt_no_any_created_group.visibility = View.VISIBLE
                        txt_no_results.visibility = View.GONE
                    } else {
                        txt_no_any_created_group.visibility = View.GONE
                        txt_no_results.visibility = View.VISIBLE
                    }
                } else {
                    txt_no_any_created_group.visibility = View.GONE
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
        override fun onGroupRequestJoinClick(position: Int, group: GroupResponse) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onGroupClick(group: GroupResponse) {
            navigator.navigateToGroupDetailActivity(this@MyGroupsFragment, group)
        }

        override fun onGroupJoinClick(position: Int, group: GroupResponse) {
            positionJoinClicked = position
            viewModel.joinGroup(group)
        }
    }

    override fun fetchGroups(searchQuery: String?) {
        viewModel.fetchGroups(viewLifecycleOwner, searchQuery)
    }
}