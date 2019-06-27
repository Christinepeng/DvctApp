package com.divercity.android.features.company.selectcompany.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.selectcompany.base.adapter.CompanyAdapter
import com.divercity.android.features.company.selectcompany.base.adapter.CompanyViewHolder
import kotlinx.android.synthetic.main.fragment_base_company.*
import kotlinx.android.synthetic.main.view_search.view.*
import javax.inject.Inject

class SelectCompanyFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: SelectCompanyViewModel

    @Inject
    lateinit var adapter: CompanyAdapter

    var fragListener: Listener? = null

    private var handlerSearch = Handler()

    companion object {

        const val REQUEST_CODE_CREATE_COMPANY = 1050

        fun newInstance(): SelectCompanyFragment {
            return SelectCompanyFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_base_company

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[SelectCompanyViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeToLiveData()
        setupSearch()
    }

    private fun subscribeToLiveData() {
        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun setupView() {
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter

        lay_action.setOnClickListener {
            navigator.navigateToCreateCompanyActivityForResult(this, REQUEST_CODE_CREATE_COMPANY)
        }

        img_action.setImageResource(R.drawable.icon_briefcase)
        txt_action_title.setText(R.string.create_new_company)

        lay_action2.setOnClickListener {
            fragListener?.onNoCurrentCompany()
        }
        img_action2.setImageResource(R.drawable.icon_briefcase)
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
        handlerSearch.removeCallbacksAndMessages(null)
        handlerSearch.postDelayed({
            viewModel.fetchData(viewLifecycleOwner, query)
        }, AppConstants.SEARCH_DELAY)
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(viewLifecycleOwner, Observer {
            txt_search_company.visibility = View.GONE
            adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(viewLifecycleOwner, Observer { networkState ->
            networkState?.let {
                adapter.currentList?.let { list ->
                    if (networkState.status == Status.SUCCESS && list.size == 0)
                        txt_no_results.visibility = View.VISIBLE
                    else {
                        txt_no_results.visibility = View.GONE
                    }
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

        fun onNoCurrentCompany()

        fun onCompanyChosen(company: CompanyResponse)
    }
}
