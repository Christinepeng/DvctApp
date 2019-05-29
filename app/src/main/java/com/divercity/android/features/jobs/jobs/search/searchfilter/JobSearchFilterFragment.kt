package com.divercity.android.features.jobs.jobs.search.searchfilter

import android.content.Context
import android.os.Bundle
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.features.dialogs.jobsearchfilter.IJobSearchFilter
import com.divercity.android.features.jobs.jobs.search.searchfilterview.JobSearchFilterViewFragment
import kotlinx.android.synthetic.main.fragment_job_search_filter.*

/**
 * Created by lucas on 24/10/2018.
 */

class JobSearchFilterFragment : BaseFragment() {

    private var listener: IJobSearchFilter? = null

    companion object {

        fun newInstance(): JobSearchFilterFragment {
            return JobSearchFilterFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_search_filter

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

        lay_view_type.setOnClickListener {
            listener?.replaceFragment(JobSearchFilterViewFragment.newInstance(), "TEST")
        }

        btn_close.setOnClickListener {
            listener?.replaceFragment(null, null)
        }
    }
}