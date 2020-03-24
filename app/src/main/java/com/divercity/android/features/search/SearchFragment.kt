package com.divercity.android.features.search

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.NavUtils
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_job_search_filter_view.view.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.view_toolbar.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import kotlinx.android.synthetic.main.view_toolbar_for_search.view.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class SearchFragment : BaseFragment() {

    private lateinit var viewModel: SearchViewModel

    @Inject
    lateinit var adapterTab: SearchViewPagerAdapter

    private var handlerSearch = Handler()

    companion object {

        private const val PARAM_SEARCH_QUERY= "paramSearchQuery"

        fun newInstance(searchQuery: String): SearchFragment {
            val fragment = SearchFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_SEARCH_QUERY, searchQuery)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
        viewModel.lastSearchQuery = arguments?.getString(PARAM_SEARCH_QUERY)!!

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupAdapterViewPager()
        subscribeToLiveData()
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.getItemId()) {
//            android.R.id.home -> {
////                navigator.navigateToHomeActivity(requireActivity())
//                NavUtils.navigateUpFromSameTask(requireActivity())
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private fun subscribeToLiveData() {

    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar_for_search)
            supportActionBar?.let {
                it.setTitle(R.string.search)
                //show arrow
                it.setDisplayHomeAsUpEnabled(true)
//                it.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.grey_background)))
//                it.setHomeAsUpIndicator(R.drawable.icon_back)
            }
        }
    }

    private fun setupAdapterViewPager() {
        viewpager.adapter = adapterTab
        tab_layout.setupWithViewPager(viewpager)

        val onPageListener = object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                (adapterTab.getRegisteredFragment(viewpager.currentItem) as? ITabSearch)
                    ?.search(viewModel.lastSearchQuery)
            }
        }
        viewpager.addOnPageChangeListener(onPageListener)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchItem.expandActionView()
        searchView.setQuery(viewModel.lastSearchQuery, true)

        (adapterTab.getRegisteredFragment(viewpager.currentItem) as? ITabSearch)
            ?.search(viewModel.lastSearchQuery)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                handlerSearch.removeCallbacksAndMessages(null)
                (adapterTab.getRegisteredFragment(viewpager.currentItem) as? ITabSearch)
                    ?.search(query)
                viewModel.lastSearchQuery = query
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handlerSearch.removeCallbacksAndMessages(null)
                handlerSearch.postDelayed({
                    (adapterTab.getRegisteredFragment(viewpager.currentItem) as? ITabSearch)
                        ?.search(newText)
                    viewModel.lastSearchQuery = newText
                }, AppConstants.SEARCH_DELAY)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}
