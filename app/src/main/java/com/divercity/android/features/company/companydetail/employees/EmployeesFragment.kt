package com.divercity.android.features.company.companydetail.employees

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
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.profile.useradapter.pagination.UserPaginationAdapter
import com.divercity.android.features.profile.useradapter.pagination.UserViewHolder
import kotlinx.android.synthetic.main.fragment_employees.*
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class EmployeesFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: EmployeesViewModel

    @Inject
    lateinit var adapter: UserPaginationAdapter

    private var isListRefreshing = false
    private var lastConnectUserPosition = 0

    companion object {

        private const val PARAM_COMPANY_ID = "paramCommpanyId"

        fun newInstance(companyId: String): EmployeesFragment {
            val fragment = EmployeesFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_COMPANY_ID, companyId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_employees

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        viewModel.fetchEmployees(arguments!!.getString(PARAM_COMPANY_ID)!!)
        initView()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    fun initViewModel(){
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)
                .get(EmployeesViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
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
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedListEmployees.observe(this, Observer {
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
                    txt_no_employee.visibility = View.VISIBLE
                else
                    txt_no_employee.visibility = View.GONE

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

        override fun onConnectUser(user: UserResponse, position: Int) {
            lastConnectUserPosition = position
            viewModel.connectToUser(user.id)
        }

        override fun onUserDirectMessage(user: UserResponse) {
            navigator.navigateToChatActivity(
                this@EmployeesFragment,
                user.userAttributes!!.name!!,
                user.id,
                null
            )
        }

        override fun onUserClick(user: UserResponse) {
            navigator.navigateToOtherUserProfileActivity(this@EmployeesFragment, null, user)
        }
    }
}