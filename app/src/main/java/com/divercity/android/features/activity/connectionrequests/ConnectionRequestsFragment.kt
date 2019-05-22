package com.divercity.android.features.activity.connectionrequests

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
import com.divercity.android.features.activity.connectionrequests.adapter.ConnectionRequestAdapter
import com.divercity.android.features.activity.connectionrequests.adapter.ConnectionRequestViewHolder
import com.divercity.android.model.position.GroupInvitationNotificationPosition
import com.divercity.android.model.position.JoinGroupRequestPosition
import com.divercity.android.model.position.UserPosition
import kotlinx.android.synthetic.main.fragment_connection_requests.*
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
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)
                .get(ConnectionRequestsViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        subscribeToPaginatedLiveData()
        subscribeToLiveData()
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

            adapter.currentList?.let { pagedList ->
                if (networkState?.status != Status.LOADING)
                    isListRefreshing = false

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
                    txt_no_requests.visibility = View.VISIBLE
                else
                    txt_no_requests.visibility = View.GONE

                swipe_list_main.isRefreshing = isListRefreshing
            }

            if (!isListRefreshing)
                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })
    }

    private fun subscribeToLiveData() {
        viewModel.acceptDeclineConnectionRequestResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }

                Status.SUCCESS -> {
                    adapter.notifyItemChanged(response.data!!.position)
                    hideProgress()
                }
            }
        })

        viewModel.acceptDeclineGroupInviteResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }

                Status.SUCCESS -> {
                    adapter.notifyItemChanged(response.data!!.position)
                    hideProgress()
                }
            }
        })

        viewModel.acceptDeclineJoinGroupRequest.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }

                Status.SUCCESS -> {
                    adapter.notifyItemChanged(response.data!!.position)
                    hideProgress()
                }
            }
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    private
    val listener: ConnectionRequestViewHolder.Listener =
        object : ConnectionRequestViewHolder.Listener {

            override fun acceptGroupInvitation(invitation: GroupInvitationNotificationPosition) {
                viewModel.acceptGroupInvitation(invitation)
            }

            override fun declineGroupInvitation(invitation: GroupInvitationNotificationPosition) {
                viewModel.declineGroupInvitation(invitation)
            }

            override fun acceptJoinGroupRequest(request: JoinGroupRequestPosition) {
                viewModel.acceptJoinGroupRequest(request)
            }

            override fun declineJoinGroupRequest(request: JoinGroupRequestPosition) {
                viewModel.declineJoinGroupRequest(request)
            }

            override fun acceptUserConnectionRequest(user: UserPosition) {
                viewModel.acceptConnectionRequest(user)
            }

            override fun declineUserConnectionRequest(user: UserPosition) {
                viewModel.declineConnectionRequest(user)
            }
        }
}