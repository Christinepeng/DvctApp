package com.divercity.android.features.company.companydetail.about

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.companydetail.CompanyDetailViewModel
import kotlinx.android.synthetic.main.fragment_company_detail_about.*

/**
 * Created by lucas on 16/11/2018.
 */

class CompanyDetailAboutFragment : BaseFragment() {

    lateinit var sharedViewModel: CompanyDetailViewModel

    companion object {

        fun newInstance(): CompanyDetailAboutFragment {
            return CompanyDetailAboutFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_company_detail_about

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(CompanyDetailViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        sharedViewModel.companyLiveData.observe(viewLifecycleOwner, Observer { company ->
            showData(company)
        })
    }

    private fun showData(company: CompanyResponse?) {
        val desc = company?.attributes?.description
        if (desc != null && desc != "") {
            txt_company_desc.text = desc
        } else {
            txt_no_description.visibility = View.VISIBLE
            txt_company_desc.visibility = View.GONE
        }
    }
}