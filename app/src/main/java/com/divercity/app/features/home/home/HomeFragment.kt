package com.divercity.app.features.home.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.data.Status
import com.divercity.app.features.home.HomeActivity
import com.divercity.app.features.home.home.featured.FeaturedAdapter
import com.divercity.app.features.home.home.feed.adapter.FeedAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class HomeFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: HomeViewModel

    @Inject
    lateinit var feedAdapter: FeedAdapter

    @Inject
    lateinit var featuredAdapter: FeaturedAdapter

    private var mIsRefreshing = false

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupEvents()
        initAdapters()
        initSwipeToRefresh()
        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as HomeActivity).apply {
            supportActionBar?.let {
                it.setTitle(R.string.divercity)
                it.setDisplayHomeAsUpEnabled(true)
                it.setHomeAsUpIndicator(R.drawable.icon_inbox)
            }
        }
    }

    private fun initAdapters() {
        feedAdapter.setRetryCallback(this)
        list_main.adapter = feedAdapter
        list_featured.adapter = featuredAdapter
    }

    private fun subscribeToLiveData() {
        viewModel.questionList.observe(this, Observer {
            feedAdapter.submitList(it)
            if (feedAdapter.getCurrentList() != null)
                Timber.e("questionList CURRENT LIST: " + feedAdapter.currentList!!.size)
        })

        viewModel.networkState.observe(this, Observer {
            if (feedAdapter.getCurrentList() != null)
                Timber.e("CURRENT LIST: " + feedAdapter.currentList!!.size)

            if (!mIsRefreshing || it!!.getStatus() === Status.ERROR || it!!.getStatus() === Status.SUCCESS)
                feedAdapter.setNetworkState(it)

            if (feedAdapter.itemCount == 1 && it!!.getStatus() == Status.LOADING) {
                viewModel.getFeatured()
            }
        })

        viewModel.refreshState.observe(this, Observer { networkState ->
            if (feedAdapter.getCurrentList() != null)
                Timber.e("CURRENT LIST: " + feedAdapter.currentList!!.size + " y " + networkState!!.status.name)
            if (networkState != null) {
                Timber.e("STATUS: " + networkState.status.name)
                if (feedAdapter.getCurrentList() != null) {
                    if (networkState.getStatus() === NetworkState.LOADED.getStatus() || networkState.getStatus() === Status.ERROR)
                        mIsRefreshing = false
                    Timber.e("STATUS size: " + feedAdapter.currentList!!.size)

                    val d = networkState.getStatus() === NetworkState.LOADING.getStatus()
                    Timber.e("STATUS: $d")

                    swipe_list_main.isRefreshing = networkState.getStatus() === NetworkState.LOADING.getStatus()
                }

                if (!mIsRefreshing)
                    swipe_list_main.isEnabled = networkState.getStatus() === Status.SUCCESS

                if (networkState.getStatus() === Status.SUCCESS && feedAdapter.currentList!!.size == 0) {
                    lay_no_groups.visibility = View.VISIBLE
                    (btn_fab as View).visibility = View.GONE
                } else {
                    lay_no_groups.visibility = View.GONE
                    (btn_fab as View).visibility = View.VISIBLE
                }
            }
        })

        viewModel.featuredList.observe(this, Observer { resource ->
            when (resource?.status) {
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
                Status.SUCCESS -> {
                    featuredAdapter.submitList(resource.data)
                }
            }
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    private fun initSwipeToRefresh() {

        swipe_list_main.apply {
            setOnRefreshListener {
                mIsRefreshing = true
                viewModel.getFeatured()
                viewModel.refresh()
            }

            setColorSchemeColors(
                    ContextCompat.getColor(context, R.color.colorPrimaryDark),
                    ContextCompat.getColor(context, R.color.colorPrimary),
                    ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_home_fragment_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                viewModel.clearUserData()
                navigator.navigateToEnterEmailActivity(activity!!)
                true
            }
            R.id.action_search -> {
                showToast()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupEvents() {
        btn_fab.setOnClickListener { showToast() }
        btn_create_group.setOnClickListener { showToast() }
        btn_explore_groups.setOnClickListener { showToast() }
    }

    private fun showToast() {
        Toast.makeText(activity!!, "Coming soon", Toast.LENGTH_SHORT).show()
    }
}