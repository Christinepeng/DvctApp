package com.divercity.android.features.company.companyadminadd

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.features.company.companyadminadd.adapter.UserSingleSelectionAdapter
import kotlinx.android.synthetic.main.fragment_company_admin_edit.*
import kotlinx.android.synthetic.main.view_search.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class CompanyAdminAddFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: CompanyAdminAddViewModel

    @Inject
    lateinit var adapter: UserSingleSelectionAdapter

    private var handlerSearch = Handler()

    companion object {

        private const val PARAM_COMPANY_ID = "paramCompanyId"

        fun newInstance(companyId: String): CompanyAdminAddFragment {
            val fragment = CompanyAdminAddFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_COMPANY_ID, companyId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_company_admin_edit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CompanyAdminAddViewModel::class.java)
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

    private fun initView(){
        initAdapter()

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
    }

    private fun initAdapter() {
        adapter.setRetryCallback(this)
        list.adapter = adapter

//        btn_continue.setOnClickListener {
//            if (adapter.selectedUsers.isNotEmpty()) {
//                CreateGroupChatFragment.selectedUsers = adapter.selectedUsers
//                navigator.navigateToCreateGroupChatActivityForResult(
//                    this,
//                    REQUEST_CODE_GROUP_CREATED
//                )
//            }
//        }
    }

    private fun search(query: String?) {
        handlerSearch.removeCallbacksAndMessages(null)
        handlerSearch.postDelayed({
            viewModel.fetchUsers(viewLifecycleOwner, query)
        }, AppConstants.SEARCH_DELAY)
    }

    private fun subscribeToLiveData() {

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedUserList.observe(this, Observer {
            adapter.lastPositionSelected = null
            adapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })

        viewModel.refreshState.observe(this, Observer { networkState ->
            adapter.currentList?.let { pagedList ->
                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
                    txt_no_results.visibility = View.VISIBLE
                else
                    txt_no_results.visibility = View.GONE
            }
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}