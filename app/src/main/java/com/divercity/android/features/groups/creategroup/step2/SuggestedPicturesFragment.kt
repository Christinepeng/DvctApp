package com.divercity.android.features.groups.creategroup.step2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_group_detail.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 25/10/2018.
 */


class SuggestedPicturesFragment : BaseFragment() {

    lateinit var viewModelCreate: SuggestedPicturesViewModel

    companion object {

        fun newInstance(): SuggestedPicturesFragment {
            return SuggestedPicturesFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_suggested_pictures

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.groups)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search_threedots, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                menu.findItem(R.id.action).isVisible = false
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                menu.findItem(R.id.action).isVisible = true
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }
}