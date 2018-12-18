package com.divercity.app.features.industry.selectsingleindustry

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import com.divercity.app.AppConstants
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.data.Status
import com.divercity.app.features.industry.selectsingleindustry.adapter.IndustrySingleAdapter
import com.divercity.app.features.industry.selectsingleindustry.adapter.IndustrySingleViewHolder
import kotlinx.android.synthetic.main.fragment_select_industry_for_company.*
import kotlinx.android.synthetic.main.view_search.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 06/11/2018.
 */

class SelectSingleIndustryFragment : BaseFragment(), RetryCallback {

    override fun layoutId(): Int = R.layout.fragment_select_industry_for_company

    @Inject
    lateinit var singleAdapter: IndustrySingleAdapter

    lateinit var viewModel: SelectSingleIndustryViewModel

    private var handlerSearch = Handler()
    private var lastSearch: String? = null

    companion object {
        const val INDUSTRY_PICKED = "locationPicked"

        fun newInstance(): SelectSingleIndustryFragment {
            return SelectSingleIndustryFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectSingleIndustryViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        subscribeToPaginatedLiveData()
        setupSearch()
    }

    private fun setupView() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_industry)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        singleAdapter.setRetryCallback(this)
        singleAdapter.setListener(listener)
        list.adapter = singleAdapter
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedIndustryList.observe(this, Observer {
            singleAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            singleAdapter.setNetworkState(it)
        })

        viewModel.refreshState.observe(this, Observer { networkState ->
            networkState?.let {
                singleAdapter.currentList?.let {
                    if (networkState.status == Status.SUCCESS && it.size == 0)
                        txt_no_results.visibility = View.VISIBLE
                    else
                        txt_no_results.visibility = View.GONE
                }
            }
        })
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

    private fun search(query: String?) {
        if (lastSearch != query) {
            handlerSearch.removeCallbacksAndMessages(null)
            handlerSearch.postDelayed({
                viewModel.fetchIndustries(this, if (query == "") null else query)
                subscribeToPaginatedLiveData()
                lastSearch = query
            }, AppConstants.SEARCH_DELAY)
        }
    }

    override fun retry() {
        viewModel.retry()
    }

    private val listener = IndustrySingleViewHolder.Listener {
        val intent = Intent()
        intent.putExtra(INDUSTRY_PICKED, it)
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}