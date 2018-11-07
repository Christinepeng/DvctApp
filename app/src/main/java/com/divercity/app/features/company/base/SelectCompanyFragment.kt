package com.divercity.app.features.company.base


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.data.Status
import com.divercity.app.data.entity.company.CompanyResponse
import com.divercity.app.features.company.base.adapter.CompanyAdapter
import com.divercity.app.features.company.base.adapter.CompanyViewHolder
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_search.view.*
import javax.inject.Inject

class SelectCompanyFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: SelectCompanyViewModel

    @Inject
    lateinit var adapter: CompanyAdapter

    var fragListener: Listener? = null


    companion object {

        fun newInstance(): SelectCompanyFragment {
            return SelectCompanyFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_list_search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectCompanyViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchCompanies(this, null)
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter
        subscribeToPaginatedLiveData()
        setupHeader()
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragListener = parentFragment as Listener
    }

    private fun setupHeader() {
        include_search.edtxt_search.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                var toSearch: String? = include_search.edtxt_search.text.toString()

                if (toSearch == "")
                    toSearch = null

                viewModel.fetchCompanies(this@SelectCompanyFragment, toSearch)
                subscribeToPaginatedLiveData()
                true
            } else
                false
        }
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedCompanyList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })

        viewModel.refreshState.observe(this, Observer { networkState ->
            networkState?.let {
                adapter.currentList?.let { list ->
                    if (networkState.status == Status.SUCCESS && list.size == 0)
                        txt_no_results.visibility = View.VISIBLE
                    else
                        txt_no_results.visibility = View.GONE
                }
            }
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    private val listener: CompanyViewHolder.Listener = CompanyViewHolder.Listener {
        fragListener?.onCompanyChosen(it)
    }

    interface Listener {

        fun onCompanyChosen(company: CompanyResponse)
    }
}
