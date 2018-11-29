package com.divercity.app.features.industry.base

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.data.Status
import com.divercity.app.data.entity.industry.IndustryResponse
import com.divercity.app.features.industry.base.adapter.IndustryAdapter
import com.divercity.app.features.industry.base.adapter.IndustryViewHolder
import kotlinx.android.synthetic.main.fragment_list_search.*
import kotlinx.android.synthetic.main.view_search.view.*
import javax.inject.Inject

class SelectIndustryFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: SelectIndustryViewModel

    @Inject
    lateinit var adapter: IndustryAdapter

    var fragListener: Listener? = null

    companion object {

        fun newInstance(): SelectIndustryFragment {
            return SelectIndustryFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_list_search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectIndustryViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchIndustries(this, null)

        setupView()
        subscribeToPaginatedLiveData()
        setupSearch()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragListener = parentFragment as Listener
    }

    private fun setupView(){
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter

        lay_action.visibility = View.GONE
    }

    private fun setupSearch() {
        include_search.edtxt_search.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                var toSearch: String? = include_search.edtxt_search.text.toString()

                if (toSearch == "")
                    toSearch = null

                viewModel.fetchIndustries(this@SelectIndustryFragment, toSearch)
                subscribeToPaginatedLiveData()
                true
            } else
                false
        }
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedIndustryList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })

        viewModel.refreshState.observe(this, Observer { networkState ->
            networkState?.let {
                adapter.currentList?.let {
                    if (networkState.status == Status.SUCCESS && it.size == 0)
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

    interface Listener {

        fun onIndustryChosen(industry: IndustryResponse)
    }

    private val listener: IndustryViewHolder.Listener = IndustryViewHolder.Listener {
        fragListener?.onIndustryChosen(it)
    }
}
