package com.divercity.android.features.location.base

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.location.LocationResponse
import com.divercity.android.features.location.base.adapter.LocationAdapter
import com.divercity.android.features.location.base.adapter.LocationViewHolder
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

    var fragListener: Listener? = null

    private var handlerSearch = Handler()

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
        subscribeToLiveData()
    }

    private fun setupView() {
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter

        img_action.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.icon_location))
        txt_action_title.setText(R.string.use_my_location)
        lay_action.visibility = View.GONE
    }

    private fun setupSearch() {

        include_search.edtxt_search.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                val toSearch: String? = include_search.edtxt_search.text.toString()

                search(toSearch)
                true
            } else
                false
        }

        include_search.edtxt_search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                search(p0.toString())
            }
        })
    }

    private fun subscribeToLiveData() {

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun search(query: String?) {
        if (viewModel.lastSearch != query) {
            handlerSearch.removeCallbacksAndMessages(null)
            handlerSearch.postDelayed({
                viewModel.fetchLocations(viewLifecycleOwner, query)
            }, AppConstants.SEARCH_DELAY)
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

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}