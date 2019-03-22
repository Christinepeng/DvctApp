package com.divercity.android.features.activity.connectionrequests

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.activity.connectionrequests.adapter.ConnectionRequestAdapter
import com.divercity.android.features.profile.currentuser.tabconnections.adapter.UserViewHolder
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class ConnectionRequestsFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: ConnectionRequestsViewModel

    @Inject
    lateinit var adapter: ConnectionRequestAdapter

    private var isListRefreshing = false

    companion object {

        fun newInstance(): ConnectionRequestsFragment {
            return ConnectionRequestsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_connection_requests

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = activity?.run {
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(ConnectionRequestsViewModel::class.java)
//        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initList()
        subscribeToPaginatedLiveData()
    }

    private fun initList() {
        adapter.setRetryCallback(this)
//        adapter.setListener(listener)
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

        txt_empty_array.setText(R.string.no_following)
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedConnectionList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(this, Observer {
            if (!isListRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(this, Observer { networkState ->

//            adapter.currentList?.let { pagedList ->
//                if (networkState?.status != Status.LOADING)
//                    isListRefreshing = false
//
//                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
//                    lay_no_followers.visibility = View.VISIBLE
//                else
//                    lay_no_followers.visibility = View.GONE
//
//                swipe_list_main.isRefreshing = isListRefreshing
//            }
//
//            if (!isListRefreshing)
//                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    private
    val listener: UserViewHolder.Listener = object : UserViewHolder.Listener {

        override fun onConnectUser(user: UserResponse, position : Int) {
        }

        override fun onUserDirectMessage(user: UserResponse) {
        }

        override fun onUserClick(user: UserResponse) {
        }
    }
}