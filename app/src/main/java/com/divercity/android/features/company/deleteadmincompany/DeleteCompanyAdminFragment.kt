package com.divercity.android.features.company.deleteadmincompany

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.features.company.deleteadmincompany.DeleteCompanyAdminActivity.Companion.INTENT_EXTRA_PARAM_COMPANY_ID
import com.divercity.android.features.company.deleteadmincompany.DeleteCompanyAdminActivity.Companion.INTENT_EXTRA_PARAM_COMPANY_OWNER_ID
import com.divercity.android.features.company.deleteadmincompany.adapter.DeleteCompanyAdminAdapter
import kotlinx.android.synthetic.main.fragment_delete_group_admin.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class DeleteCompanyAdminFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: DeleteCompanyAdminViewModel

    @Inject
    lateinit var adapter: DeleteCompanyAdminAdapter

    private var handlerSearch = Handler()

    companion object {

        fun newInstance(data: Bundle?): DeleteCompanyAdminFragment {
            val fragment = DeleteCompanyAdminFragment()
            fragment.arguments = data
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_delete_group_admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)
                .get(DeleteCompanyAdminViewModel::class.java)
        viewModel.companyId = arguments?.getString(INTENT_EXTRA_PARAM_COMPANY_ID) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.edit_admins)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        viewModel.fetchData()

        initView()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun initView() {
        initAdapter()

        include_search.visibility = View.GONE

        btn_action.setOnClickListener {
            val selected = adapter.getSelectedUsersIds()
            if (selected.isNotEmpty()) {
                viewModel.deleteCompanyAdmins(
                    adapter.getSelectedUsersIds()
                )
            } else {
                showToast("Select at least one")
            }
        }

//        include_search.edtxt_search.setOnKeyListener { _, keyCode, keyEvent ->
//            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//
//                val toSearch: String? = include_search.edtxt_search.text.toString()
//
//                search(toSearch)
//                true
//            } else
//                false
//        }
//
//        include_search.edtxt_search.addTextChangedListener(object : TextWatcher {
//
//            override fun afterTextChanged(p0: Editable?) {
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                search(p0.toString())
//            }
//        })
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun initAdapter() {
        adapter.ownerId = arguments?.getString(INTENT_EXTRA_PARAM_COMPANY_OWNER_ID)!!
        adapter.setRetryCallback(this)
        list.adapter = adapter
    }

    private fun search(query: String?) {
        handlerSearch.removeCallbacksAndMessages(null)
        handlerSearch.postDelayed({
            viewModel.fetchData(viewLifecycleOwner, query)
        }, AppConstants.SEARCH_DELAY)
    }

    private fun subscribeToLiveData() {

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })

        viewModel.deleteCompanyAdminResponse.observe(viewLifecycleOwner, Observer { response ->
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
                    viewModel.refresh()
                }
            }
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(viewLifecycleOwner, Observer {
            adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(viewLifecycleOwner, Observer { networkState ->
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