package com.divercity.app.features.profile.tabprofile

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment

/**
 * Created by lucas on 16/11/2018.
 */

class TabProfileFragment : BaseFragment() {

    companion object {

        fun newInstance(): TabProfileFragment {
            return TabProfileFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_tab_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    fun setupView() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODE_LOCATION && resultCode == Activity.RESULT_OK) {
//            val location =
//                    data?.extras?.getParcelable<LocationResponse>(ToolbarLocationFragment.LOCATION_PICKED)
//            setLocation(location)
//        } else if (requestCode == REQUEST_CODE_COMPANY && resultCode == Activity.RESULT_OK) {
//            val company =
//                    data?.extras?.getParcelable<CompanyResponse>(ToolbarCompanyFragment.COMPANY_PICKED)
//            setCompany(company)
//        } else if (requestCode == REQUEST_CODE_JOBTYPE && resultCode == Activity.RESULT_OK) {
//            val jobType =
//                    data?.extras?.getParcelable<JobTypeResponse>(JobTypeFragment.JOBTYPE_PICKED)
//            setJobType(jobType)
//        } else if (requestCode == REQUEST_CODE_SKILLS && resultCode == Activity.RESULT_OK) {
//            val skillList =
//                    data?.extras?.getParcelableArrayList<SkillResponse>(JobSkillsFragment.SKILLS_TITLE)
//            skillList?.let {
//                setSkills(skillList)
//            }
//        }
//        checkFormIsCompleted()
    }
}