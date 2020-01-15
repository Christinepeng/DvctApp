package com.divercity.android.features.company.diversityrating

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.companydetail.CompanyDetailViewModel
import com.divercity.android.features.company.diversityrating.adapter.DiversityRatingAdapter
import com.divercity.android.features.dialogs.InviteToCreateCompanyReviewDialogFragment
import kotlinx.android.synthetic.main.fragment_diversity_rating.*
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class DiversityRatingFragment(companyResponse: CompanyResponse?) : BaseFragment(), RetryCallback {

    lateinit var viewModel: DiversityRatingViewModel
    lateinit var sharedViewModel: CompanyDetailViewModel

    @Inject
    lateinit var adapter: DiversityRatingAdapter

    private var isListRefreshing = false
    private val companyResponse = companyResponse

    companion object {

        const val REQUEST_CODE_RATE_COMPANY = 200

        fun newInstance(companyResponse: CompanyResponse?): DiversityRatingFragment {
            return DiversityRatingFragment(companyResponse)
        }
    }

    override fun layoutId(): Int = R.layout.fragment_diversity_rating

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        viewModel.start(sharedViewModel.companyLiveData.value?.id!!)
        initView()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(DiversityRatingViewModel::class.java)

        sharedViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(CompanyDetailViewModel::class.java)
    }

    private fun subscribeToLiveData() {
        sharedViewModel.companyLiveData.observe(viewLifecycleOwner, Observer { company ->
            adapter.setCompany(company)
        })
    }

    private fun initView() {
        adapter.setRetryCallback(this)
        list_reviews.adapter = adapter

        swipe_list_main.apply {
            setOnRefreshListener {
                isListRefreshing = true
                refresh()
            }
            isEnabled = false
            setColorSchemeColors(
                ContextCompat.getColor(context, R.color.colorPrimaryDark),
                ContextCompat.getColor(context, R.color.colorPrimary),
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
        }
    }

    private fun refresh() {
        viewModel.refresh()
        sharedViewModel.fetchCompany()
    }

    fun fetchLiveData() {
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
        showInviteToCreateCompanyReviewDialogFragmentOrNot(companyResponse, this)
    }

    private fun showInviteToCreateCompanyReviewDialogFragmentOrNot(
        companyResponse: CompanyResponse?,
        fragment: DiversityRatingFragment
    ) {
        if (companyResponse?.attributes?.canRateCompany == true
            && companyResponse?.attributes?.hasRatedBefore == false) {
            val dialog = InviteToCreateCompanyReviewDialogFragment.newInstance()
            dialog.listener = object : InviteToCreateCompanyReviewDialogFragment.Listener {
                override fun onSubmit() {
                    navigator.navigateToRateCompany(
                        fragment,
                        companyResponse,
                        false,
                        REQUEST_CODE_RATE_COMPANY
                    )
                }
            }
            dialog.show(childFragmentManager, null)
        }
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList().observe(viewLifecycleOwner, Observer {
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
                    txt_no_reviews.visibility = View.VISIBLE
                else {
                    txt_no_reviews.visibility = View.GONE
                }

                swipe_list_main.isRefreshing = isListRefreshing
            }

            if (!isListRefreshing)
                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })
    }

    override fun retry() {
        viewModel.retry()
    }
}