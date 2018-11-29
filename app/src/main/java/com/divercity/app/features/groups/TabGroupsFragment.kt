package com.divercity.app.features.groups

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
import android.widget.Toast
import com.divercity.app.AppConstants
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.ViewPagerDotsPanel
import com.divercity.app.data.Status
import com.divercity.app.data.entity.group.recommendedgroups.RecommendationItem
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

    @Inject
    lateinit var recommendedGroupsAdapter: TabGroupsPanelViewPagerAdapter

    private var handlerSearch = Handler()
    private var lastSearchQuery: String? = ""
    var lastPositionJoinClick = 0
    lateinit var handlerViewPager: Handler

    private lateinit var searchView : SearchView
    private var searchItem: MenuItem?= null

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

        handlerViewPager = Handler()

        setupToolbar()
        setupAdapterViewPager()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModelTab.fetchRecommendedGroupsResponse.observe(viewLifecycleOwner, Observer {
            it?.let { group ->
                if (group.status == Status.SUCCESS) {
                    img_default_group.visibility = View.GONE
                    initAdapterRecommendedGroups(it.data?.data?.recommendation)
                }
            }
        })

        viewModelTab.joinGroupResponse.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { group ->
                when (group.status) {
                    Status.LOADING -> showProgress()

                    Status.ERROR -> {
                        hideProgress()
                        Toast.makeText(activity, group.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        hideProgress()
                        recommendedGroupsAdapter.updateView(lastPositionJoinClick)
                    }
                }
            }
        })
    }

    private fun initAdapterRecommendedGroups(list: List<RecommendationItem>?) {
        if(list?.size != 0) {
            lay_carousel_viewpager.visibility = View.VISIBLE

            slider_viewpager.adapter = recommendedGroupsAdapter

            recommendedGroupsAdapter.listener = object : TabGroupsPanelViewPagerAdapter.Listener {

                override fun onBtnJoinClicked(jobId: Int?, position: Int) {
                    jobId?.let {
                        lastPositionJoinClick = position
                        viewModelTab.joinGroup(jobId)
                    }
                }
            }

            recommendedGroupsAdapter.setList(list)

            val viewPagerDotsPanel = ViewPagerDotsPanel(
                    context,
                    recommendedGroupsAdapter.count,
                    sliderDots
            )

            slider_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {
                    viewPagerDotsPanel.onPageSelected(position)
                    handlerViewPager.removeCallbacksAndMessages(null)
                    handlerViewPager.postDelayed(runnable, AppConstants.CAROUSEL_PAGES_DELAY)
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })

            handlerViewPager.postDelayed(runnable, AppConstants.CAROUSEL_PAGES_DELAY)
        } else {
            lay_carousel_viewpager.visibility = View.GONE
        }
    }

    private val runnable = object : Runnable {
        override fun run() {
            slider_viewpager.currentItem =
                    if (slider_viewpager.currentItem == recommendedGroupsAdapter.count - 1)
                        0
                    else
                        slider_viewpager.currentItem + 1
        }
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
        val onPageListener = object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                (adapterTab.getRegisteredFragment(viewpager.currentItem) as? ITabsGroups)
                    ?.fetchGroups(lastSearchQuery)
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

        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                lay_carousel_viewpager.visibility = View.GONE
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                lay_carousel_viewpager.visibility = View.VISIBLE
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
                }, AppConstants.SEARCH_DELAY)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        handlerViewPager.removeCallbacksAndMessages(null)
        handlerSearch.removeCallbacksAndMessages(null)

        searchView.setOnQueryTextListener(null)
        searchItem?.setOnActionExpandListener(null)
        searchItem = null

        lastSearchQuery = ""
        super.onDestroyView()
    }
}
