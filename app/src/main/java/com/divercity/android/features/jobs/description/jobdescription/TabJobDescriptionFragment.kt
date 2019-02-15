package com.divercity.android.features.jobs.description.jobdescription

import android.os.Bundle
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_job_description.*

/**
 * Created by lucas on 16/11/2018.
 */
 
class TabJobDescriptionFragment : BaseFragment(){

    companion object {

        private const val PARAM_JOB_DESC = "paramJobDesc"

        fun newInstance(description : String?): TabJobDescriptionFragment {
            val fragment = TabJobDescriptionFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_JOB_DESC, description)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_description

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){
        webview.loadDataWithBaseURL(null
            , arguments?.getString(PARAM_JOB_DESC),
            "text/html", "UTF-8",
            "")
    }
}