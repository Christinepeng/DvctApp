package com.divercity.android.features.jobs

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.features.home.HomeActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_jobs.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class TabJobsFragment : BaseFragment() {

    private lateinit var viewModel: TabJobsViewModel

    @Inject
    lateinit var adapterTab: TabJobsViewPagerAdapter

    private var searchView: SearchView? = null
    private var searchItem: MenuItem? = null

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

        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(TabJobsViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")

        setupToolbar()
        setupAdapterViewPager()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.showBtnAddJob.observe(this, Observer { data ->
            data?.let {
                (btn_add as View).visibility = data
                btn_add.setOnClickListener {
                    navigator.navigateToJobPostingForResultActivity(this, -1, null)
                }
            }
        })
    }

    private fun setupToolbar() {
        (activity as HomeActivity).apply {
            icon_notification.visibility = View.GONE
            supportActionBar?.let {
                it.setTitle(R.string.job_board)
                it.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    private fun setupAdapterViewPager() {
        viewpager.adapter = adapterTab
        viewModel.adapterPosition?.apply {
            viewpager.currentItem = this
        }
        tab_layout.setupWithViewPager(viewpager)

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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView?.queryHint = getString(R.string.search)

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

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

        searchView?.setOnQueryTextListener(null)
        searchItem?.setOnActionExpandListener(null)
        searchItem = null

        viewModel.adapterPosition = viewpager.currentItem

        lastSearchQuery = ""
        super.onDestroyView()
    }
}
