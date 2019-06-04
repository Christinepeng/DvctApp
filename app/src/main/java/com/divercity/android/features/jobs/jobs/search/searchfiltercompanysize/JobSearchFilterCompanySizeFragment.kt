package com.divercity.android.features.jobs.jobs.search.searchfiltercompanysize

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.company.companysize.CompanySizesViewModel
import com.divercity.android.features.dialogs.jobsearchfilter.IJobSearchFilter
import com.divercity.android.features.dialogs.jobsearchfilter.JobSearchFilterDialogViewModel
import com.divercity.android.features.jobs.jobs.search.searchfiltercompanysize.adapter.CompanySizeMultipleSelectionAdapter
import kotlinx.android.synthetic.main.fragment_job_search_filter_company_size.*
import kotlinx.android.synthetic.main.view_retry.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

class JobSearchFilterCompanySizeFragment : BaseFragment() {

    lateinit var viewModel: JobSearchFilterDialogViewModel

    lateinit var viewModelCompanySizes: CompanySizesViewModel

    @Inject
    lateinit var adapter: CompanySizeMultipleSelectionAdapter

    private var listener: IJobSearchFilter? = null

    companion object {

        fun newInstance(): JobSearchFilterCompanySizeFragment {
            return JobSearchFilterCompanySizeFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_search_filter_company_size

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
        viewModelCompanySizes =
            ViewModelProviders.of(this, viewModelFactory)[CompanySizesViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeToLiveData()
    }

    private fun setupView() {
        adapter.selectedSizes = viewModel.selectedSizes
        adapter.onSelectUnselectSize = {
            btn_select_all.isSelected = false
        }

        list.adapter = adapter

        btn_select_all.isSelected = viewModel.isAllSizesSelected
        btn_back.setOnClickListener {
            if (btn_select_all.isSelected)
                viewModel.companySize.value = "All Sizes"
            else
                viewModel.companySize.value = adapter.getSelectedSizesString()
            viewModel.isAllSizesSelected = btn_select_all.isSelected
            viewModel.selectedSizes = adapter.selectedSizes
            listener?.onBackPressed()
        }

        lay_all.setOnClickListener {
            btn_select_all.isSelected = !btn_select_all.isSelected
            if (btn_select_all.isSelected) {
                adapter.onSelectAll()
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModelCompanySizes.fetchCompanySizeResponse.observe(
            viewLifecycleOwner,
            Observer { response ->
                when (response?.status) {
                    Status.LOADING -> {
                        showRetry(false)
                        showProgressNoBk()
                    }

                    Status.ERROR -> {
                        showRetry(true)
                        hideProgressNoBk()
                        showToast(response.message)
                    }

                    Status.SUCCESS -> {
                        showRetry(false)
                        hideProgressNoBk()
                        adapter.submitList(response.data)
                    }
                }
            })
    }

    private fun showRetry(boolean: Boolean) {
        if (boolean) {
            include_retry.btn_retry.setOnClickListener {
                viewModelCompanySizes.fetchCompanySizes()
            }
            include_retry.visibility = View.VISIBLE
        } else
            include_retry.visibility = View.GONE
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }
}