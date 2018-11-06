package com.divercity.app.features.home.jobs.mypostings

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment

/**
 * Created by lucas on 25/10/2018.
 */


class MyPostingsFragment : BaseFragment() {

    lateinit var viewModel: MyPostingsViewModel

    companion object {

        fun newInstance(): MyPostingsFragment {
            return MyPostingsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_my_postings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(MyPostingsViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_jobs, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView  = searchItem.actionView as SearchView
        searchView.queryHint = "Search"

        viewModel.strSearchQuery?.let {
            if(it != ""){
                searchItem.expandActionView()
                searchView.setQuery(it, true)
                searchView.clearFocus()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.strSearchQuery = query
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }
}