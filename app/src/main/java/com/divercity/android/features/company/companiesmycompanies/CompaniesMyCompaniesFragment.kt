package com.divercity.android.features.company.companiesmycompanies

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.companies.adapter.TabCompaniesViewHolder
import com.divercity.android.features.company.companiesmycompanies.adapter.CompanyAdapter
import com.divercity.android.features.company.companiesmycompanies.adapter.CompanyMyCompaniesAdapter
import com.divercity.android.features.company.companiesmycompanies.adapter.MyCompaniesViewHolder
import com.divercity.android.features.search.ITabSearch
import kotlinx.android.synthetic.main.fragment_companies_my_companies.*
import javax.inject.Inject

class CompaniesMyCompaniesFragment : BaseFragment(), RetryCallback, ITabSearch {

    lateinit var viewModel: CompaniesMyCompaniesViewModel

    @Inject
    lateinit var adapter: CompanyMyCompaniesAdapter

    @Inject
    lateinit var myCompaniesAdapter: CompanyAdapter

    private var handlerSearch = Handler()

    private var isListRefreshing = false

    companion object {

        fun newInstance(): CompaniesMyCompaniesFragment {
            return CompaniesMyCompaniesFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_companies_my_companies

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(CompaniesMyCompaniesViewModel::class.java)
        setupView()
        initSwipeToRefresh()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })

        viewModel.fetchMyCompaniesResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    showToast(response.message)
                }

                Status.SUCCESS -> {
                    viewModel.showMyCompaniesSection.value = response.data!!.isNotEmpty()
                    myCompaniesAdapter.list = response.data
                }
            }
        })

        viewModel.showMyCompaniesSection.observe(viewLifecycleOwner, Observer {
            adapter.showMyCompaniesSection = it
        })
    }

    private fun setupView() {
        adapter.showMyCompaniesSection = viewModel.showMyCompaniesSection.value!!

        adapter.setRetryCallback(this)
        adapter.setListener(listener)

        myCompaniesAdapter.listener = listener
        adapter.myCompaniesListener = object : MyCompaniesViewHolder.Listener {

            override fun onSeeAll() {
                navigator.navigateToMyCompanies(requireActivity())
            }
        }
        adapter.myCompaniesAdapter = myCompaniesAdapter

        list.adapter = adapter
    }

    override fun search(searchQuery: String?) {
        viewModel.fetchData(viewLifecycleOwner, searchQuery)
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList().observe(viewLifecycleOwner, Observer {
            // To hide my companies section when searching
            viewModel.showMyCompaniesSection.value =
                viewModel.lastSearch.isNullOrEmpty() && myCompaniesAdapter.list.isNotEmpty()
            adapter.submitList(it)
        })

        viewModel.networkState().observe(viewLifecycleOwner, Observer {
            if (!isListRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(viewLifecycleOwner, Observer { networkState ->
            adapter.currentList?.let { pagedList ->
                if (networkState?.status != Status.LOADING)
                    isListRefreshing = false

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
                    txt_no_results.visibility = View.VISIBLE
                else {
                    txt_no_results.visibility = View.GONE
                }

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

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun retry() {
        viewModel.fetchMyCompanies()
        viewModel.retry()
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    private val listener = object : TabCompaniesViewHolder.Listener {

        override fun onCompanyClick(company: CompanyResponse) {
            navigator.navigateToCompanyDetail(this@CompaniesMyCompaniesFragment, company)
        }
    }
}
