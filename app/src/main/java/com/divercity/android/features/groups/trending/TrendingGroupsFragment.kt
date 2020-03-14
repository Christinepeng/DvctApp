package com.divercity.android.features.groups.trending

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
import com.divercity.android.features.groups.adapter.GroupsAdapter
import com.divercity.android.features.groups.viewmodel.GroupViewModel
import com.divercity.android.features.search.ITabSearch
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class TrendingGroupsFragment : BaseFragment(), RetryCallback, ITabSearch {

    lateinit var viewModel: TrendingGroupsViewModel

    lateinit var groupViewModel: GroupViewModel

    @Inject
    lateinit var adapter: GroupsAdapter

    private var isListRefreshing = false

    companion object {

        fun newInstance(): TrendingGroupsFragment {
            return TrendingGroupsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_list_refresh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)[TrendingGroupsViewModel::class.java]
        } ?: throw Exception("Invalid Fragment")

        groupViewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)[GroupViewModel::class.java]
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setRetryCallback(this)
//        adapter.setListener(listener)
        list.adapter = adapter
        initSwipeToRefresh()
        subscribeToPaginatedLiveData()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {

        groupViewModel.joinPublicGroupResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    adapter.reloadPosition(response.data!!.position)
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    adapter.updatePositionOnJoinPublicGroup(response.data!!)
                }
            }
        })

        groupViewModel.requestToJoinPrivateGroupResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    adapter.reloadPosition(response.data!!.position)
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    adapter.updatePositionOnJoinPrivateGroupRequest(response.data!!)
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
//
//    private val listener = object : GroupsViewHolder.Listener {
//
//        override fun onGroupRequestJoinClick(groupPosition: GroupPositionModel) {
//            viewModel.requestToJoinGroup(groupPosition)
//        }
//
//        override fun onGroupJoinClick(groupPosition: GroupPositionModel) {
//            viewModel.joinGroup(groupPosition)
//        }
//
//        override fun onGroupClick(group: GroupResponse) {
//            navigator.navigateToGroupDetailForResult(this@TrendingGroupsFragment, group)
//        }
//    }

    override fun search(searchQuery: String?) {
        viewModel.fetchGroups(viewLifecycleOwner, searchQuery)
    }
}