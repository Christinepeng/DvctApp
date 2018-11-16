package com.divercity.app.features.jobs

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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


class TabJobsFragment : BaseFragment() {

    lateinit var viewModelTab: TabJobsViewModel

    @Inject
    lateinit var adapterTab: TabJobsViewPagerAdapter

    companion object {

        fun newInstance(): TabJobsFragment {
            return TabJobsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_jobs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelTab = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(TabJobsViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")

        setupToolbar()
        setupAdapterViewPager()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModelTab.showBtnAddJob.observe(this, Observer { data ->
            data?.let {
                (btn_add as View).visibility = data
                btn_add.setOnClickListener {
                    navigator.navigateToJobPostingActivity(activity!!)
                }
            }
        })
    }

    private fun setupToolbar() {
        (activity as HomeActivity).apply {
            supportActionBar?.let {
                it.setTitle(R.string.job_board)
                it.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    private fun setupAdapterViewPager() {
        viewpager.adapter = adapterTab
        tab_layout.setupWithViewPager(viewpager)
    }
}
