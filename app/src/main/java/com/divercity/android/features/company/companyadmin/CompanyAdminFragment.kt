package com.divercity.android.features.company.companyadmin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminResponse
import com.divercity.android.features.company.companyadmin.adapter.CompanyAdminAdapter
import com.divercity.android.features.company.companyadmin.adapter.CompanyAdminViewHolder
import kotlinx.android.synthetic.main.fragment_company_admin.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class CompanyAdminFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: CompanyAdminViewModel

    @Inject
    lateinit var adapter: CompanyAdminAdapter

    private var isListRefreshing = false

    private lateinit var companyId: String

    companion object {

        const val REQUEST_CODE_ADMIN = 200

        private const val PARAM_COMPANY_ID = "paramCompanyId"

        fun newInstance(companyId: String): CompanyAdminFragment {
            val fragment = CompanyAdminFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_COMPANY_ID, companyId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_company_admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CompanyAdminViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        companyId = arguments!!.getString(PARAM_COMPANY_ID)!!
        viewModel.fetchCompanyAdmins(companyId)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.admins)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

//        lay_new_group.setOnClickListener {
//            navigator.navigateToNewGroupChatActivityForResult(this, REQUEST_CODE_GROUP_CREATED)
//        }

        btn_edit.visibility = View.GONE

        initView()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun initView() {
        initAdapter()
        lay_add_admin.setOnClickListener {
            navigator.navigateToCompanyAddAdminForResult(
                this@CompanyAdminFragment,
                companyId,
                REQUEST_CODE_ADMIN
            )
        }
    }

    private fun initAdapter() {
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

    private fun subscribeToLiveData() {

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
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

                swipe_list_main.isRefreshing = isListRefreshing
            }

            if (!isListRefreshing)
                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    private val listener: CompanyAdminViewHolder.Listener =
        object : CompanyAdminViewHolder.Listener {

            override fun onUserClick(userId: String) {
            }

            override fun onConnectUser(admin: CompanyAdminResponse, position: Int) {
            }

            override fun onUserDirectMessage(admin: CompanyAdminResponse) {
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADMIN && Activity.RESULT_OK == resultCode) {
            viewModel.refresh()
        }
    }
}