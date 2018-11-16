package com.divercity.app.features.jobs.saved

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.data.Status
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.features.jobs.saved.adapter.SavedJobsAdapter
import com.divercity.app.features.jobs.saved.adapter.SavedJobsViewHolder
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class SavedJobsFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: SavedJobsViewModel

    @Inject
    lateinit var adapter: SavedJobsAdapter

    private var isListRefreshing = false
    private var positionSaveUnsavedClicked: Int = 0

    companion object {

        fun newInstance(): SavedJobsFragment {
            return SavedJobsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_list_refresh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(SavedJobsViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwipeToRefresh()
        initAdapter()
        subscribeToPaginatedLiveData()
        subscribeJobLiveData()
    }

    private fun initAdapter() {
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter
    }

    private fun subscribeJobLiveData() {
        viewModel.jobResponse.observe(this, Observer { job ->
            when (job?.status) {
                Status.LOADING -> {
//                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, job.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    adapter.currentList?.get(positionSaveUnsavedClicked)?.attributes?.isBookmarkedByCurrent =
                            job.data?.attributes?.isBookmarkedByCurrent
                    adapter.notifyItemChanged(positionSaveUnsavedClicked)
                }
            }
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedJobsList?.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(this, Observer {
            if (!isListRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(this, Observer { networkState ->

            adapter.currentList?.let { pagedList ->
                if (networkState?.status != Status.LOADING)
                    isListRefreshing = false

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
                    txt_no_results.visibility = View.VISIBLE
                else
                    txt_no_results.visibility = View.GONE

                swipe_list_main.isRefreshing = isListRefreshing
            }

            if (!isListRefreshing)
                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })
    }

    private fun initSwipeToRefresh() {

        swipe_list_main.apply {
            setOnRefreshListener {
                isListRefreshing = true
                viewModel.refresh()
            }
            isEnabled = false
            setColorSchemeColors(
                    ContextCompat.getColor(context, R.color.colorPrimaryDark),
                    ContextCompat.getColor(context, R.color.colorPrimary),
                    ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
        }
    }

    override fun retry() {
        viewModel.retry()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search)

        viewModel.strSearchQuery?.let {
            if (it != "") {
                searchItem.expandActionView()
                searchView.setQuery(it, true)
                searchView.clearFocus()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.strSearchQuery = query
                viewModel.fetchJobs(query)
                subscribeToPaginatedLiveData()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                viewModel.fetchJobs(null)
                viewModel.strSearchQuery = null
                subscribeToPaginatedLiveData()
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    private val listener: SavedJobsViewHolder.Listener = object : SavedJobsViewHolder.Listener {

        override fun onJobClick(job: JobResponse) {

        }
    }
}