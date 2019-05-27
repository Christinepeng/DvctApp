package com.divercity.android.features.jobs.jobs.search

import android.os.Bundle
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 24/10/2018.
 */

class JobSearchFilterFragment : BaseFragment() {

    companion object {

        fun newInstance(): JobSearchFilterFragment {
            return JobSearchFilterFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_search_filter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}