package com.divercity.android.features.profile.tabconnections

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.profile.tabconnections.adapter.UserAdapter
import com.divercity.android.features.profile.tabconnections.adapter.UserViewHolder
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class ConnectionsFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: ConnectionsViewModel

    @Inject
    lateinit var adapter: UserAdapter

    private var isListRefreshing = false

    companion object {

        fun newInstance(): ConnectionsFragment {
            return ConnectionsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_list_refresh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(ConnectionsViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        subscribeToPaginatedLiveData()
    }

    private fun initList() {
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

        txt_empty_array.setText(R.string.no_connection)
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedApplicantsList.observe(this, Observer {
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
                    lay_no_followers.visibility = View.VISIBLE
                else
                    lay_no_followers.visibility = View.GONE

                swipe_list_main.isRefreshing = isListRefreshing
            }

            if (!isListRefreshing)
                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    private val listener: UserViewHolder.Listener = object : UserViewHolder.Listener {

        override fun onUserFollow(user: UserResponse) {
        }

        override fun onUserDirectMessage(user: UserResponse) {
        }

        override fun onUserClick(user: UserResponse) {
        }
    }
}