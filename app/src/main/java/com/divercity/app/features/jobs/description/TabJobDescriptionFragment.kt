package com.divercity.app.features.jobs.description

import android.os.Bundle
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
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
        txt_job_desc.text = arguments?.getString(PARAM_JOB_DESC)
    }
}