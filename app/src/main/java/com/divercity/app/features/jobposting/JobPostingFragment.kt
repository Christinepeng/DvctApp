package com.divercity.app.features.jobposting

import android.os.Bundle
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_job_posting.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 05/11/2018.
 */
 
class JobPostingFragment : BaseFragment() {

    companion object {

        fun newInstance() = JobPostingFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_job_posting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
    }

    private fun setupToolbar() {
        (activity as JobPostingActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.create_job_posting)
                it.setDisplayHomeAsUpEnabled(true)
                it.setHomeAsUpIndicator(R.drawable.ic_close_24dp)
            }
        }
    }

    private fun setupView(){
        lay_location.setOnClickListener {
            navigator.navigateToSelectLocationActivity(activity!!,"")
        }
    }
}