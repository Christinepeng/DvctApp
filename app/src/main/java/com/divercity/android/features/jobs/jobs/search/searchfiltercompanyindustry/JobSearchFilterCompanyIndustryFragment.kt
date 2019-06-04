package com.divercity.android.features.jobs.jobs.search.searchfiltercompanyindustry

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
import com.divercity.android.features.dialogs.jobsearchfilter.IJobSearchFilter
import com.divercity.android.features.dialogs.jobsearchfilter.JobSearchFilterDialogViewModel
import com.divercity.android.features.industry.selectsingleindustry.SelectSingleIndustryViewModel
import com.divercity.android.features.jobs.jobs.search.searchfiltercompanyindustry.adapter.IndustryMultipleAdapter
import kotlinx.android.synthetic.main.fragment_job_search_filter_industry.*
import kotlinx.android.synthetic.main.view_search.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

class JobSearchFilterCompanyIndustryFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: JobSearchFilterDialogViewModel

    @Inject
    lateinit var adapter: IndustryMultipleAdapter

    lateinit var viewModelIndustry: SelectSingleIndustryViewModel

    private var listener: IJobSearchFilter? = null

    private var handlerSearch = Handler()

    companion object {

        fun newInstance(): JobSearchFilterCompanyIndustryFragment {
            return JobSearchFilterCompanyIndustryFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_search_filter_industry

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            // To know if the dialog is being called from an activity or fragment
            parentFragment?.let {
                listener = it as IJobSearchFilter
            }
            if (listener == null)
                listener = context as IJobSearchFilter
        } catch (e: ClassCastException) {
            throw ClassCastException("Calling context must implement JobPostedDialogFragment.Listener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(
            requireActivity(),
            viewModelFactory
        )[JobSearchFilterDialogViewModel::class.java]
        viewModelIndustry = ViewModelProviders.of(this, viewModelFactory)
            .get(SelectSingleIndustryViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initList()
        setupSearch()
        subscribeToPaginatedLiveData()
        subscribeToLiveData()
    }

    private fun initView() {
        btn_select_all.isSelected = viewModel.isAllIndustriesSelected
        btn_back.setOnClickListener {
            if (btn_select_all.isSelected)
                viewModel.companyIndustry.value = "All"
            else
                viewModel.companyIndustry.value = adapter.getSelectedIndustriesString()
            viewModel.isAllIndustriesSelected = btn_select_all.isSelected
            viewModel.selectedIndustries = adapter.selectedIndustries
            listener?.onBackPressed()
        }

        lay_all.setOnClickListener {
            btn_select_all.isSelected = !btn_select_all.isSelected
            if (btn_select_all.isSelected) {
                adapter.onSelectAll()
            }
        }
    }

    private fun initList() {
        adapter.selectedIndustries = viewModel.selectedIndustries
        adapter.setRetryCallback(this)
        adapter.onSelectUnselectIndustry = {
            btn_select_all.isSelected = false
        }
        list.adapter = adapter
    }

    private fun subscribeToLiveData() {
        viewModelIndustry.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModelIndustry.pagedList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModelIndustry.networkState().observe(viewLifecycleOwner, Observer {
            adapter.setNetworkState(it)
        })

        viewModelIndustry.refreshState().observe(viewLifecycleOwner, Observer { networkState ->
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
        viewModelIndustry.retry()
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
            viewModelIndustry.fetchData(viewLifecycleOwner, query)
        }, AppConstants.SEARCH_DELAY)
    }
}