package com.divercity.android.features.company.companydetail.about

import android.os.Bundle
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_company_detail_about.*

/**
 * Created by lucas on 16/11/2018.
 */

class CompanyDetailAboutFragment : BaseFragment() {

    companion object {

        private const val PARAM_COMPANY_DESC = "paramCompanyDesc"

        fun newInstance(description: String?): CompanyDetailAboutFragment {
            val fragment = CompanyDetailAboutFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_COMPANY_DESC, description)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_company_detail_about

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val desc = arguments?.getString(PARAM_COMPANY_DESC)
        if (desc != null && desc != "") {
            txt_company_desc.text = desc
        } else {
            txt_no_description.visibility = View.VISIBLE
            txt_company_desc.visibility = View.GONE
        }
    }
}