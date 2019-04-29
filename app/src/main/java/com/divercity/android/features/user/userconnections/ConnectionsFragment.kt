package com.divercity.android.features.user.userconnections

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
import com.divercity.android.features.user.allconnections.AllConnectionsFragment
import com.divercity.android.features.user.useradapter.pagination.UserPaginationAdapter
import com.divercity.android.features.user.useradapter.pagination.UserViewHolder
import com.divercity.android.model.user.User
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class ConnectionsFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: ConnectionsViewModel

    @Inject
    lateinit var adapter: UserPaginationAdapter

    private var isListRefreshing = false

    private var lastUserPositionTap = 0
    private var lastConnectUserPosition = 0

    companion object {

        private const val PARAM_USER_ID = "paramUserId"
        const val REQUEST_CODE_USER = 200

        fun newInstance(userId: String): ConnectionsFragment {
            val fragment = ConnectionsFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_USER_ID, userId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_list_refresh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)
                .get(ConnectionsViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchConnections(arguments!!.getString(PARAM_USER_ID)!!)
        initView()
        subscribeToPaginatedLiveData()
        subscribeToLiveData()
    }

    private fun initView() {
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

        if (viewModel.isCurrentUser())
            txt_empty_array.setText(R.string.current_no_connections)
        else
            txt_empty_array.setText(R.string.other_no_connections)
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
                    lay_no_data.visibility = View.VISIBLE
                else
                    lay_no_data.visibility = View.GONE

                swipe_list_main.isRefreshing = isListRefreshing
            }

            if (!isListRefreshing)
                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })
    }

    private fun subscribeToLiveData() {
        viewModel.connectUserResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    adapter.updateRowOnRequestUserConnection(lastConnectUserPosition, null)
                    showToast(response.message)
                }

                Status.SUCCESS -> {
                    adapter.updateRowOnRequestUserConnection(lastConnectUserPosition, response.data)
                }
            }
        })
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    override fun retry() {
        viewModel.retry()
    }

    private val listener: UserViewHolder.Listener = object : UserViewHolder.Listener {

        override fun onConnectUser(user: User, position: Int) {
            lastConnectUserPosition = position
            viewModel.connectToUser(user.id)
        }

        override fun onUserDirectMessage(user: User) {
            navigator.navigateToChatActivity(
                this@ConnectionsFragment,
                user.name!!,
                user.id,
                null
            )
        }

        override fun onUserClick(user: User, position: Int) {
            lastUserPositionTap = position
            navigator.navigateToOtherUserProfileForResult(this@ConnectionsFragment, null, user, REQUEST_CODE_USER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AllConnectionsFragment.REQUEST_CODE_USER) {
            adapter.notifyItemChanged(lastUserPositionTap)
        }
    }
}