package com.divercity.android.features.home.people.companies

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.home.people.ITabPeople
import com.divercity.android.features.home.people.companies.adapter.TabCompaniesAdapter
import com.divercity.android.features.home.people.companies.adapter.TabCompaniesViewHolder
import kotlinx.android.synthetic.main.fragment_companies.*
import javax.inject.Inject

class CompaniesFragment : BaseFragment(), RetryCallback, ITabPeople {

    lateinit var viewModel: CompaniesViewModel

    @Inject
    lateinit var adapter: TabCompaniesAdapter

    private var handlerSearch = Handler()
    private var isListRefreshing = false

    companion object {

        fun newInstance(): CompaniesFragment {
            return CompaniesFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_companies

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(CompaniesViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        setupView()
        initSwipeToRefresh()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun setupView() {
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter
    }

    override fun search(query: String?) {
        viewModel.fetchCompanies(viewLifecycleOwner, query)
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedCompanyList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (!isListRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                adapter.setNetworkState(it)
        })

        viewModel.refreshState.observe(viewLifecycleOwner, Observer { networkState ->
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

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    private val listener = object : TabCompaniesViewHolder.Listener {

        override fun onCompanyClick(company: CompanyResponse) {
            navigator.navigateToCompanyDetail(this@CompaniesFragment, company)
        }
    }
}
