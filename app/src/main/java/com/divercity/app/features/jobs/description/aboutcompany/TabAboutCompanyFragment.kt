package com.divercity.app.features.jobs.description.aboutcompany

import android.os.Bundle
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.data.entity.job.response.Employer
import kotlinx.android.synthetic.main.fragment_about_company.*

/**
 * Created by lucas on 16/11/2018.
 */
 
class TabAboutCompanyFragment : BaseFragment(){

    companion object {

        private const val PARAM_COMPANY_DESC = "paramCompanyDesc"

        fun newInstance(employer : Employer?): TabAboutCompanyFragment {
            val fragment = TabAboutCompanyFragment()
            val arguments = Bundle()
            arguments.putParcelable(PARAM_COMPANY_DESC, employer)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_about_company

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){

        arguments?.getParcelable<Employer>(PARAM_COMPANY_DESC)?.also {

            GlideApp.with(this)
                    .load(it.photos?.thumb)
                    .into(img_company)

            txt_company_name.text = it.name
            txt_company_desc.text = ""
        }
    }
}