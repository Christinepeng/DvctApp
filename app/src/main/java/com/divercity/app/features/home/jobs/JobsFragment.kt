package com.divercity.app.features.home.jobs

import android.os.Bundle
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.features.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_jobs.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class JobsFragment : BaseFragment() {

    lateinit var viewModel: JobsViewModel

    @Inject
    lateinit var adapter: JobsViewPagerAdapter

    companion object {

        fun newInstance(): JobsFragment {
            return JobsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_jobs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupAdapterViewPager()
        btn_add.setOnClickListener {
            navigator.navigateToJobPostingActivity(activity!!)
        }
    }

    private fun setupToolbar() {
        (activity as HomeActivity).apply {
            supportActionBar?.let {
                it.setTitle(R.string.job_board)
                it.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    private fun setupAdapterViewPager(){
        viewpager.adapter = adapter
        tab_layout.setupWithViewPager(viewpager)
    }
}
