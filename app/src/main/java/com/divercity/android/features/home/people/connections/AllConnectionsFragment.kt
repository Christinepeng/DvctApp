package com.divercity.android.features.home.people.connections

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.profile.currentuser.tabconnections.adapter.UserAdapter
import com.divercity.android.features.profile.currentuser.tabconnections.adapter.UserViewHolder
import kotlinx.android.synthetic.main.fragment_connections.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class AllConnectionsFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: AllConnectionsViewModel

    @Inject
    lateinit var adapter: UserAdapter

    private var handlerSearch = Handler()
    private var isListRefreshing = false

    private var lastConnectUserPosition = -1

    companion object {

        fun newInstance(): AllConnectionsFragment {
            return AllConnectionsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_connections

    fun initViewModel() {
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(AllConnectionsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupView()
        initSwipeToRefresh()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun setupView() {
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
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

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun search(query: String?) {
        handlerSearch.removeCallbacksAndMessages(null)
        handlerSearch.postDelayed({
            fetchConnections(query)
        }, AppConstants.SEARCH_DELAY)
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedUserList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            if (!isListRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                adapter.setNetworkState(it)
        })

        viewModel.refreshState.observe(this, Observer { networkState ->
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

    private fun fetchConnections(searchQuery: String?) {
        viewModel.fetchConnections(viewLifecycleOwner, searchQuery)
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    override fun retry() {
        viewModel.retry()
    }

    private val listener: UserViewHolder.Listener = object : UserViewHolder.Listener {

        override fun onConnectUser(user: UserResponse, position : Int) {
            lastConnectUserPosition = position
            viewModel.connectToUser(user.id)
        }

        override fun onUserDirectMessage(user: UserResponse) {
        }

        override fun onUserClick(user: UserResponse) {
            navigator.navigateToOtherUserProfileActivity(this@AllConnectionsFragment, null, user)
        }
    }
}