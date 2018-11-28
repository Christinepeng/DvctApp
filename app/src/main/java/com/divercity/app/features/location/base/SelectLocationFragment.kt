package com.divercity.app.features.location.base

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.KeyEvent
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.data.Status
import com.divercity.app.data.entity.location.LocationResponse
import com.divercity.app.features.location.base.adapter.LocationAdapter
import com.divercity.app.features.location.base.adapter.LocationViewHolder
import kotlinx.android.synthetic.main.fragment_list_search.*
import kotlinx.android.synthetic.main.view_search.view.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class SelectLocationFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: SelectLocationViewModel

    @Inject
    lateinit var adapter: LocationAdapter

    var fragListener : Listener? = null

    companion object {

        fun newInstance(): SelectLocationFragment {
            return SelectLocationFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_list_search

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragListener = parentFragment as Listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectLocationViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupSearch()
        subscribeToPaginatedLiveData()
    }

    private fun setupView() {
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter

        img_action.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.icon_location))
        txt_action_title.setText(R.string.use_my_location)
        lay_action.visibility = View.GONE
    }

    private fun setupSearch() {
        include_search.edtxt_search.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                var toSearch: String? = include_search.edtxt_search.text.toString()

                if (toSearch == "")
                    toSearch = null

                viewModel.fetchLocations(toSearch)
                subscribeToPaginatedLiveData()
                true
            } else
                false
        }
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedLocationList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })

        viewModel.refreshState.observe(this, Observer { networkState ->
            networkState?.let {
                adapter.currentList?.let { list ->
                    if (networkState.status == Status.SUCCESS && list.size == 0)
                        txt_no_results.visibility = View.VISIBLE
                    else
                        txt_no_results.visibility = View.GONE
                }
            }
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    private val listener: LocationViewHolder.Listener = object : LocationViewHolder.Listener {

        override fun onLocationClick(location: LocationResponse) {
            fragListener?.onLocationChoosen(location)
        }
    }

    interface Listener {

        fun onLocationChoosen(location: LocationResponse)
    }
}