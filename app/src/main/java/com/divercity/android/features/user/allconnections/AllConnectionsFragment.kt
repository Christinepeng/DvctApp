package com.divercity.android.features.user.allconnections

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.features.search.ITabSearch
import com.divercity.android.features.user.useradapter.pagination.UserPaginationAdapter
import com.divercity.android.features.user.useradapter.pagination.UserViewHolder
import com.divercity.android.model.user.User
import kotlinx.android.synthetic.main.fragment_connections.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class AllConnectionsFragment : BaseFragment(), RetryCallback, ITabSearch {

    lateinit var viewModel: AllConnectionsViewModel

    @Inject
    lateinit var adapter: UserPaginationAdapter

    private var handlerSearch = Handler()
    private var isListRefreshing = false

    private var lastUserPositionTap = 0
    private var lastConnectUserPosition = 0

    companion object {

        const val REQUEST_CODE_USER = 200

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
        viewModel.connectUserResponse.observe(viewLifecycleOwner, Observer { response ->
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

    override fun search(searchQuery: String?) {
        fetchConnections(searchQuery)
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
        viewModel.fetchData(viewLifecycleOwner, searchQuery)
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
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
                this@AllConnectionsFragment,
                user.name!!,
                user.id,
                null
            )
        }

        override fun onUserClick(user: User, position: Int) {
            lastUserPositionTap = position
            navigator.navigateToOtherUserProfileForResult(this@AllConnectionsFragment, null, user, REQUEST_CODE_USER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_USER) {
            adapter.notifyItemChanged(lastUserPositionTap)
        }
    }
}