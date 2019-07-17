package com.divercity.android.features.dialogs.jobsearchfilter.search.searchfilter

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.features.dialogs.jobsearchfilter.IJobSearchFilter
import com.divercity.android.features.dialogs.jobsearchfilter.JobSearchFilterDialogViewModel
import com.divercity.android.features.dialogs.jobsearchfilter.search.searchfiltercompanyindustry.JobSearchFilterCompanyIndustryFragment
import com.divercity.android.features.dialogs.jobsearchfilter.search.searchfiltercompanysize.JobSearchFilterCompanySizeFragment
import com.divercity.android.features.dialogs.jobsearchfilter.search.searchfilterlocation.JobSearchFilterLocationFragment
import com.divercity.android.features.dialogs.jobsearchfilter.search.searchfilterview.JobSearchFilterViewFragment
import kotlinx.android.synthetic.main.fragment_job_search_filter.*
import kotlinx.android.synthetic.main.view_job_filter.view.*

/**
 * Created by lucas on 24/10/2018.
 */

class JobSearchFilterFragment : BaseFragment() {

    private var listener: IJobSearchFilter? = null
    lateinit var viewModel: JobSearchFilterDialogViewModel

    companion object {

        fun newInstance(): JobSearchFilterFragment {
            return JobSearchFilterFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_search_filter

    override fun onAttach(context: Context) {
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lay_view.txt_title.setText(R.string.view)
        lay_location.txt_title.setText(R.string.location)
        lay_job_type.txt_title.setText(R.string.job_type)
        lay_required_exp.txt_title.setText(R.string.required_exp)
        lay_company_size.txt_title.setText(R.string.company_size)
        lay_industry.txt_title.setText(R.string.industry)

        lay_view.setOnClickListener {
            listener?.replaceFragment(JobSearchFilterViewFragment.newInstance(), null)
        }

        lay_location.setOnClickListener {
            listener?.replaceFragment(JobSearchFilterLocationFragment.newInstance(), null)
        }

        lay_company_size.setOnClickListener {
            listener?.replaceFragment(JobSearchFilterCompanySizeFragment.newInstance(), null)
        }

        lay_industry.setOnClickListener {
            listener?.replaceFragment(JobSearchFilterCompanyIndustryFragment.newInstance(), null)
        }

        btn_close.setOnClickListener {
            listener?.replaceFragment(null, null)
        }

//        txt_company.text =
//            viewModel.companySize.value.plus(", ").plus(viewModel.companyIndustry.value)

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.viewTypeFilter.observe(viewLifecycleOwner, Observer {
            lay_view.txt_subtitle.text = it
        })

        viewModel.locationFilter.observe(viewLifecycleOwner, Observer {
            lay_location.txt_subtitle.text = it
        })
    }
}