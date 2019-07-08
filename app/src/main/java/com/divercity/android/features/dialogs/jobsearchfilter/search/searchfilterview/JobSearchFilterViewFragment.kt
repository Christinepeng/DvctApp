package com.divercity.android.features.dialogs.jobsearchfilter.search.searchfilterview

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.features.dialogs.jobsearchfilter.IJobSearchFilter
import com.divercity.android.features.dialogs.jobsearchfilter.JobSearchFilterDialogViewModel
import kotlinx.android.synthetic.main.fragment_job_search_filter_view.*

/**
 * Created by lucas on 24/10/2018.
 */

class JobSearchFilterViewFragment : BaseFragment() {

    lateinit var viewModel: JobSearchFilterDialogViewModel

    private var listener: IJobSearchFilter? = null

    companion object {

        fun newInstance(): JobSearchFilterViewFragment {
            return JobSearchFilterViewFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_search_filter_view

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
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)[JobSearchFilterDialogViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_back.setOnClickListener {
            listener?.onBackPressed()
        }

        lay_new.setOnClickListener {
            viewModel.viewTypeFilter.value = "New"
        }

        lay_recommended.setOnClickListener {
            viewModel.viewTypeFilter.value = "Recommended"
        }

        lay_all.setOnClickListener {
            viewModel.viewTypeFilter.value = "All"
        }

        viewModel.viewTypeFilter.observe(viewLifecycleOwner, Observer {
            when(it){
                "All" -> {
                    btn_select_all.isSelected = true
                    btn_select_recommended.isSelected = false
                    btn_select_new.isSelected = false
                }
                "Recommended" -> {
                    btn_select_all.isSelected = false
                    btn_select_recommended.isSelected = true
                    btn_select_new.isSelected = false
                }
                "New" -> {
                    btn_select_all.isSelected = false
                    btn_select_recommended.isSelected = false
                    btn_select_new.isSelected = true
                }
            }
        })
    }
}