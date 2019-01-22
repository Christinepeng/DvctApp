package com.divercity.android.features.company.base

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.base.adapter.CompanyAdapter
import com.divercity.android.features.company.base.adapter.CompanyViewHolder
import kotlinx.android.synthetic.main.fragment_list_search.*
import kotlinx.android.synthetic.main.view_search.view.*
import javax.inject.Inject

class SelectCompanyFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: SelectCompanyViewModel

    @Inject
    lateinit var adapter: CompanyAdapter

    var fragListener: Listener? = null

    private var handlerSearch = Handler()
    private var lastSearch: String? = null

    companion object {

        const val REQUEST_CODE_CREATE_COMPANY = 1050

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
        setupView()
        subscribeToPaginatedLiveData()
        setupSearch()
    }

    private fun setupView() {
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter

        lay_action.setOnClickListener {
            navigator.navigateToCreateCompanyActivityForResult(this, REQUEST_CODE_CREATE_COMPANY)
        }

        img_action.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.icon_briefcase))
        txt_action_title.setText(R.string.create_new_company)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragListener = parentFragment as Listener
    }

    private fun setupSearch() {
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

    private fun search(query: String?) {
        if(lastSearch != query) {
            handlerSearch.removeCallbacksAndMessages(null)
            handlerSearch.postDelayed({
                viewModel.fetchCompanies(this@SelectCompanyFragment, if (query == "") null else query)
                subscribeToPaginatedLiveData()
                lastSearch = query
            }, AppConstants.SEARCH_DELAY)
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

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    private val listener: CompanyViewHolder.Listener = CompanyViewHolder.Listener {
        fragListener?.onCompanyChosen(it)
    }

    interface Listener {

        fun onCompanyChosen(company: CompanyResponse)
    }
}
