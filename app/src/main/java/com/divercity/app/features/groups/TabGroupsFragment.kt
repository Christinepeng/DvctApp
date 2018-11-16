package com.divercity.app.features.groups

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.features.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_groups.*
import javax.inject.Inject


/**
 * Created by lucas on 25/10/2018.
 */


class TabGroupsFragment : BaseFragment() {

    lateinit var viewModelTab: TabGroupsViewModel

    @Inject
    lateinit var adapterTab: TabGroupsViewPagerAdapter

    private var handlerSearch = Handler()
    private var lastSearchQuery: String? = ""

    companion object {

        fun newInstance(): TabGroupsFragment {
            return TabGroupsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_groups

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelTab = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(TabGroupsViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")

        setupToolbar()
        setupAdapterViewPager()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {

    }

    private fun setupToolbar() {
        (activity as HomeActivity).apply {
            supportActionBar?.let {
                it.setTitle(R.string.groups)
                it.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    private fun setupAdapterViewPager() {
        viewpager.adapter = adapterTab
        tab_layout.setupWithViewPager(viewpager)
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                (adapterTab.getRegisteredFragment(viewpager.currentItem) as? ITabsGroups)
                    ?.fetchGroups(lastSearchQuery)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search)

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                lay_viewpager.visibility = View.GONE
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                lay_viewpager.visibility = View.VISIBLE
                return true
            }

        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                handlerSearch.removeCallbacksAndMessages(null)
                (adapterTab.getRegisteredFragment(viewpager.currentItem) as? ITabsGroups)
                        ?.fetchGroups(query)
                lastSearchQuery = query
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handlerSearch.removeCallbacksAndMessages(null)
                handlerSearch.postDelayed({
                    (adapterTab.getRegisteredFragment(viewpager.currentItem) as? ITabsGroups)
                            ?.fetchGroups(newText)
                    lastSearchQuery = newText
                }, 2000)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }
}
