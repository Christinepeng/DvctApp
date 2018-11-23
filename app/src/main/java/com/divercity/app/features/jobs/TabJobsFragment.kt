package com.divercity.app.features.jobs

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.divercity.app.AppConstants
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

    private lateinit var searchView : SearchView
    private var searchItem: MenuItem?= null

    private var handlerSearch = Handler()
    private var lastSearchQuery: String? = ""

    companion object {

        fun newInstance(): TabJobsFragment {
            return TabJobsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_jobs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        val onPageListener = object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                (adapterTab.getRegisteredFragment(viewpager.currentItem) as? ITabJobs)
                        ?.fetchJobs(lastSearchQuery)
            }
        }
        viewpager.addOnPageChangeListener(onPageListener)
        viewpager.adapter = adapterTab
        tab_layout.setupWithViewPager(viewpager)


        viewpager.post {
            onPageListener.onPageSelected(0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                handlerSearch.removeCallbacksAndMessages(null)
                (adapterTab.getRegisteredFragment(viewpager.currentItem) as? ITabJobs)
                        ?.fetchJobs(query)
                lastSearchQuery = query
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handlerSearch.removeCallbacksAndMessages(null)
                handlerSearch.postDelayed({
                    (adapterTab.getRegisteredFragment(viewpager.currentItem) as? ITabJobs)
                            ?.fetchJobs(newText)
                    lastSearchQuery = newText
                }, AppConstants.SEARCH_DELAY)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)

        searchView.setOnQueryTextListener(null)
        searchItem?.setOnActionExpandListener(null)
        searchItem = null

        lastSearchQuery = ""
        super.onDestroyView()
    }
}
