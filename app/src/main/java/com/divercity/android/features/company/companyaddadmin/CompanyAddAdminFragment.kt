package com.divercity.android.features.company.companyaddadmin

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.company.companyaddadmin.adapter.AdminSelectedAdapter
import com.divercity.android.features.company.companyaddadmin.adapter.AdminSelectedViewHolder
import com.divercity.android.features.profile.useradapter.charpaginationmultiplesel.UserCharPagMultiSelAdapter
import kotlinx.android.synthetic.main.fragment_company_add_admin.*
import kotlinx.android.synthetic.main.view_search.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class CompanyAddAdminFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: CompanyAddAdminViewModel

    @Inject
    lateinit var adapterUsers: UserCharPagMultiSelAdapter

    @Inject
    lateinit var adapterAdmin: AdminSelectedAdapter

    private var handlerSearch = Handler()

    private var companyId: String? = null

    companion object {

        private const val PARAM_COMPANY_ID = "paramCompanyId"

        fun newInstance(companyId: String): CompanyAddAdminFragment {
            val fragment = CompanyAddAdminFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_COMPANY_ID, companyId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_company_add_admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CompanyAddAdminViewModel::class.java)
        companyId = arguments?.getString(PARAM_COMPANY_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.add_admin)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
        initView()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun initView() {

        list_admins.layoutManager = StaggeredGridLayoutManager(2, 1)
        adapterAdmin.listener = object : AdminSelectedViewHolder.Listener {

            override fun onAdminRemoved(data: UserResponse) {
                adapterUsers.selectedUsers.remove(data)
                adapterAdmin.admins?.remove(data)
                adapterAdmin.notifyDataSetChanged()
                adapterUsers.notifyDataSetChanged()
            }
        }
        list_admins.adapter = adapterAdmin

        adapterUsers.setRetryCallback(this)
        adapterUsers.onSelectUnselectUser = {
            adapterAdmin.admins = it
        }
        list.adapter = adapterUsers

        include_search.edtxt_search.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                val toSearch: String? = include_search.edtxt_search.text.toString()

                search(toSearch)
                true
            } else
                false
        }

        include_search.edtxt_search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                search(p0.toString())
            }
        })

        btn_add.setOnClickListener {
            if (companyId != null) {
                if (adapterAdmin.getAdminsIds().isNotEmpty())
                    viewModel.addCompanyAdmins(companyId!!, adapterAdmin.getAdminsIds())
                else
                    showToast(getString(R.string.select_at_least_one))
            } else
                showToast("Error")
        }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun search(query: String?) {
        handlerSearch.removeCallbacksAndMessages(null)
        handlerSearch.postDelayed({
            viewModel.fetchData(viewLifecycleOwner, query)
        }, AppConstants.SEARCH_DELAY)
    }

    private fun subscribeToLiveData() {

        viewModel.addCompanyAdminResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    showToast("Admins added successfully")
                    finishSuccess()
                }
            }
        })

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList.observe(this, Observer {
            adapterUsers.submitList(it)
        })

        viewModel.networkState().observe(this, Observer {
            adapterUsers.setNetworkState(it)
        })

        viewModel.refreshState().observe(this, Observer { networkState ->
            adapterUsers.currentList?.let { pagedList ->
                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
                    txt_no_results.visibility = View.VISIBLE
                else
                    txt_no_results.visibility = View.GONE
            }
        })
    }

    private fun finishSuccess() {
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun retry() {
        viewModel.retry()
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}