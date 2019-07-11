package com.divercity.android.features.company.diversityrating

import android.app.Activity
import android.content.Intent
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
import kotlinx.android.synthetic.main.fragment_diversity_rating.*
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class DiversityRatingFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: DiversityRatingViewModel
    lateinit var sharedViewModel: CompanyDetailViewModel

    @Inject
    lateinit var adapter: DiversityRatingAdapter

    private var isListRefreshing = false

    companion object {

        const val REQUEST_CODE_RATE_COMPANY = 200

        fun newInstance(): DiversityRatingFragment {
            return DiversityRatingFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_diversity_rating

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        viewModel.fetchCompanyDiversityReviews(sharedViewModel.companyLiveData.value?.id!!)
        initView()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun initViewModel() {
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(DiversityRatingViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        sharedViewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(CompanyDetailViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    private fun subscribeToLiveData() {
        sharedViewModel.companyLiveData.observe(viewLifecycleOwner, Observer { company ->
            showData(company)
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

    private fun refresh(){
        sharedViewModel.fetchCompany()
        viewModel.refresh()
    }

    private fun showData(company: CompanyResponse?) {
        company?.attributes?.also {
            rating_bar_header.rating = it.divercityRating?.totalDivercityRating?.toFloat() ?: 0f

            txt_rating.text = it.divercityRating?.totalDivercityRating.toString()
            if (it.divercityRating != null && it.divercityRating.totalDivercityRating != 0)
                txt_rating.visibility = View.VISIBLE
            else
                txt_rating.visibility = View.GONE

            if (it.canRateCompany == true) {
                btn_rate.visibility = View.VISIBLE
                btn_rate.setOnClickListener {
                    navigator.navigateToRateCompany(this, company, REQUEST_CODE_RATE_COMPANY)
                }
            } else
                btn_rate.visibility = View.GONE

            txt_ratings.text = (it.divercityRating?.totalDivercityRating.toString()).plus(" Ratings")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_RATE_COMPANY){
                refresh()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}