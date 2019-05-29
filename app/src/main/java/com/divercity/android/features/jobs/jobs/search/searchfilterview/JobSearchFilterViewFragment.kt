package com.divercity.android.features.jobs.jobs.search.searchfilterview

import android.content.Context
import android.os.Bundle
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.features.dialogs.jobsearchfilter.IJobSearchFilter
import kotlinx.android.synthetic.main.fragment_job_search_filter_view.*

/**
 * Created by lucas on 24/10/2018.
 */

class JobSearchFilterViewFragment : BaseFragment() {

    private var listener: IJobSearchFilter? = null

    companion object {

        fun newInstance(): JobSearchFilterViewFragment {
            return JobSearchFilterViewFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_search_filter_view

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_back.setOnClickListener {
            listener?.onBackPressed()
        }
    }
}