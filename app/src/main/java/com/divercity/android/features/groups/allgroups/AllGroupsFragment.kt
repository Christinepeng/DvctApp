package com.divercity.android.features.groups.allgroups

import android.content.Intent
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
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.groups.adapter.GroupsAdapter
import com.divercity.android.features.groups.adapter.GroupsViewHolder
import com.divercity.android.features.search.ITabSearch
import com.divercity.android.model.position.GroupPosition
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class AllGroupsFragment : BaseFragment(), RetryCallback, ITabSearch {

    lateinit var viewModel: AllGroupsViewModel

    @Inject
    lateinit var adapter: GroupsAdapter

    private var isListRefreshing = false
    private var lastGroupPositionTap = 0

    companion object {

        const val REQUEST_CODE_GROUP = 200

        fun newInstance(): AllGroupsFragment {
            return AllGroupsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_list_refresh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(AllGroupsViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter
        initSwipeToRefresh()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.joinPublicGroupResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    adapter.reloadPosition(response.data!!.position)
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    adapter.updatePositionOnJoinPublicGroup(response.data!!)
                }
            }
        })

        viewModel.requestToJoinPrivateGroupResponse.observe(
            viewLifecycleOwner,
            Observer { response ->
                when (response?.status) {
                    Status.LOADING -> {
                    }

                    Status.ERROR -> {
                        adapter.reloadPosition(response.data!!.position)
                        Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        adapter.updatePositionOnJoinRequest(response.data!!)
                    }
                }
            })

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

        override fun onGroupRequestJoinClick(groupPosition: GroupPosition) {
            viewModel.requestToJoinGroup(groupPosition)
        }

        override fun onGroupJoinClick(groupPosition: GroupPosition) {
            viewModel.joinGroup(groupPosition)
        }

        override fun onGroupClick(position: Int, group: GroupResponse) {
            lastGroupPositionTap = position
            navigator.navigateToGroupDetailForResult(this@AllGroupsFragment, group, REQUEST_CODE_GROUP)
        }

    }

    override fun search(searchQuery: String?) {
        viewModel.fetchData(viewLifecycleOwner, searchQuery)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GROUP) {
            adapter.notifyItemChanged(lastGroupPositionTap)
        }
    }
}