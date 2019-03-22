package com.divercity.android.features.home.people

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.viewpager.widget.ViewPager
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.features.home.HomeActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_people.*
import javax.inject.Inject


/**
 * Created by lucas on 25/10/2018.
 */
class TabPeopleFragment : BaseFragment() {

    @Inject
    lateinit var adapterTab: TabPeopleViewPagerAdapter

    private var handlerSearch = Handler()
    private var lastSearchQuery: String? = ""

    private var searchView: SearchView? = null
    private var searchItem: MenuItem? = null

    companion object {

        fun newInstance(): TabPeopleFragment {
            return TabPeopleFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_people

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        initView()
        setupAdapterViewPager()
        subscribeToLiveData()
    }

    private fun initView() {
        btn_create_edit_group.setOnClickListener {
            navigator.navigateToCreateGroupStep1(this)
        }
    }

    private fun subscribeToLiveData() {

    }

    private fun setupToolbar() {
        (activity as HomeActivity).apply {
            icon_notification.visibility = View.GONE
            supportActionBar?.let {
                it.setTitle(R.string.people)
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
//                (adapterTab.getRegisteredFragment(viewpager.currentItem) as? ITabsGroups)
//                        ?.fetchGroups(lastSearchQuery)
            }
        }
        viewpager.addOnPageChangeListener(onPageListener)
        viewpager.adapter = adapterTab
        tab_layout.setupWithViewPager(viewpager)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView?.queryHint = getString(R.string.search)

        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
//                lay_carousel_viewpager.visibility = View.GONE
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
//                lay_carousel_viewpager.visibility = View.VISIBLE
                return true
            }

        })

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
//                handlerSearch.removeCallbacksAndMessages(null)
//                (adapterTab.getRegisteredFragment(viewpager.currentItem) as? ITabsGroups)
//                        ?.fetchGroups(query)
//                lastSearchQuery = query
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                handlerSearch.removeCallbacksAndMessages(null)
//                handlerSearch.postDelayed({
//                    (adapterTab.getRegisteredFragment(viewpager.currentItem) as? ITabsGroups)
//                            ?.fetchGroups(newText)
//                    lastSearchQuery = newText
//                }, AppConstants.SEARCH_DELAY)
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

        lastSearchQuery = ""
        super.onDestroyView()
    }
}
