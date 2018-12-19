package com.divercity.app.core.navigation

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.divercity.app.R
import com.divercity.app.core.utils.OnboardingProgression
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.data.entity.skills.SkillResponse
import com.divercity.app.features.company.companysize.CompanySizesActivity
import com.divercity.app.features.company.createcompany.CreateCompanyActivity
import com.divercity.app.features.company.onboarding.OnboardingCompanyActivity
import com.divercity.app.features.company.withtoolbar.ToolbarCompanyActivity
import com.divercity.app.features.ethnicity.onboarding.OnboardingEthnicityActivity
import com.divercity.app.features.groups.onboarding.SelectGroupActivity
import com.divercity.app.features.home.HomeActivity
import com.divercity.app.features.industry.onboarding.SelectIndustryOnboardingActivity
import com.divercity.app.features.industry.selectsingleindustry.SelectSingleIndustryActivity
import com.divercity.app.features.jobposting.JobPostingActivity
import com.divercity.app.features.jobposting.jobtype.JobTypeActivity
import com.divercity.app.features.jobposting.sharetogroup.ShareJobGroupActivity
import com.divercity.app.features.jobposting.skills.JobSkillsActivity
import com.divercity.app.features.jobs.applicants.JobApplicantsActivity
import com.divercity.app.features.jobs.description.detail.JobDetailActivity
import com.divercity.app.features.jobs.description.poster.JobDescriptionPosterActivity
import com.divercity.app.features.linkedin.LinkedinActivity
import com.divercity.app.features.location.onboarding.OnboardingLocationActivity
import com.divercity.app.features.location.withtoolbar.ToolbarLocationActivity
import com.divercity.app.features.login.step1.EnterEmailActivity
import com.divercity.app.features.login.step2.LoginActivity
import com.divercity.app.features.onboarding.profileprompt.ProfilePromptActivity
import com.divercity.app.features.onboarding.selectage.SelectAgeActivity
import com.divercity.app.features.onboarding.selectgender.SelectGenderActivity
import com.divercity.app.features.onboarding.selectinterests.SelectInterestsActivity
import com.divercity.app.features.onboarding.selectmajor.SelectMajorActivity
import com.divercity.app.features.onboarding.selectoccupation.SelectOccupationActivity
import com.divercity.app.features.onboarding.selectoccupationofinterests.SelectOOIActivity
import com.divercity.app.features.onboarding.selectschool.SelectSchoolActivity
import com.divercity.app.features.onboarding.selectusertype.SelectUserTypeActivity
import com.divercity.app.features.signup.SignUpActivity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by lucas on 25/10/2018.
 */

@Singleton
class Navigator @Inject constructor() {

    fun navigateToHomeActivity(activity: FragmentActivity) {
        navigateToHomeActivity(activity, false)
    }

    fun navigateToHomeActivity(activity: FragmentActivity, boolean: Boolean) {
        val intent = HomeActivity.getCallingIntent(activity, boolean)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        activity.startActivity(intent)
//        activity.overridePendingTransition(0,0)
    }

    fun navigateToEnterEmailActivity(activity: FragmentActivity) {
        val intent = EnterEmailActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }

    fun navigateToJobDescriptionSeekerActivity(activity: FragmentActivity, jobId: String?) {
        activity.startActivity(JobDetailActivity.getCallingIntent(activity, jobId))
    }

    fun navigateToSignUpActivity(activity: FragmentActivity, email: String) {
        activity.startActivity(SignUpActivity.getCallingIntent(activity, email))
    }

    fun navigateToLoginActivity(activity: FragmentActivity, email: String) {
        activity.startActivity(LoginActivity.getCallingIntent(activity, email))
    }

    fun navigateToProfilePromptActivity(activity: FragmentActivity) {
        activity.startActivity(ProfilePromptActivity.getCallingIntent(activity))
    }

    fun navigateToSelectUserTypeActivity(activity: FragmentActivity) {
        activity.startActivity(SelectUserTypeActivity.getCallingIntent(activity))
    }

    fun navigateToSelectSchoolActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectSchoolActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectGroupActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectGroupActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectCompanyActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(OnboardingCompanyActivity.getCallingIntent(activity, progress))
    }

    fun navigateToOnboardingLocationActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(OnboardingLocationActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectOccupationActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectOccupationActivity.getCallingIntent(activity, progress))
    }

    fun navigateToOnboardingIndustryActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectIndustryOnboardingActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectGenderActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectGenderActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectInterestsActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectInterestsActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectBirthdayActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectAgeActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectEthnicityActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(OnboardingEthnicityActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectMajorActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectMajorActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectOOIActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectOOIActivity.getCallingIntent(activity, progress))
    }

    fun navigateToLinkedinActivity(activity: FragmentActivity) {
        activity.startActivity(LinkedinActivity.getCallingIntent(activity))
    }

    fun navigateToJobPostingForResultActivity(fragment: Fragment, code : Int, job: JobResponse?) {
        fragment.startActivityForResult(JobPostingActivity.getCallingIntent(fragment.context, job), code)
    }

    fun navigateToCreateCompanyActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(CreateCompanyActivity.getCallingIntent(fragment.context), code)
    }

    fun navigateToToolbarLocationActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(ToolbarLocationActivity.getCallingIntent(fragment.context), code)
    }

    fun navigateToSelectSingleIndustryActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(SelectSingleIndustryActivity.getCallingIntent(fragment.context), code)
    }

    fun navigateToToolbarCompanyActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(ToolbarCompanyActivity.getCallingIntent(fragment.context), code)
    }

    fun navigateToCompanySizesActivity(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(CompanySizesActivity.getCallingIntent(fragment.context), code)
    }

    fun navigateToJobTypeActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(JobTypeActivity.getCallingIntent(fragment.context), code)
    }

    fun navigateToJobSkillsActivityForResult(fragment: Fragment, code: Int, skillList: ArrayList<SkillResponse>) {
        fragment.startActivityForResult(JobSkillsActivity.getCallingIntent(fragment.context, skillList), code)
    }

    fun navigateToShareJobGroupActivity(fragment: Fragment, jobId: String?) {
        fragment.startActivity(ShareJobGroupActivity.getCallingIntent(fragment.context, jobId))
    }

    fun navigateToJobDescriptionPosterForResultActivity(fragment: Fragment, code : Int, job: JobResponse) {
        fragment.startActivityForResult(JobDescriptionPosterActivity.getCallingIntent(fragment.context, job), code)
    }

    fun navigateToJobApplicantsActivity(fragment: Fragment, job: JobResponse) {
        fragment.startActivity(JobApplicantsActivity.getCallingIntent(fragment.context, job))
    }

    fun navigateToNextOnboarding(activity: FragmentActivity,
                                 userTypeId: String,
                                 cprogress: Int,
                                 shouldIncrementProgress: Boolean) {
        var progress = cprogress
        if (shouldIncrementProgress) {
            progress = OnboardingProgression.getNextNavigationProgressOnboarding(activity, userTypeId, progress)
        }

        if (activity is ProfilePromptActivity) {

            if (userTypeId == activity.getString(R.string.student_id))
                navigateToSelectSchoolActivity(activity, progress)
            else
                navigateToSelectCompanyActivity(activity, progress)
        } else if (activity is OnboardingCompanyActivity) {

            if (userTypeId == activity.getString(R.string.recruiter_id) || userTypeId == activity.getString(R.string.hiring_manager_id))
                navigateToOnboardingLocationActivity(activity, progress)
            else if (userTypeId == activity.getString(R.string.professional_id) || userTypeId == activity.getString(R.string.job_seeker_id))
                navigateToSelectOccupationActivity(activity, progress)
            else if (userTypeId == activity.getString(R.string.entrepreneur_id)) {
                navigateToOnboardingIndustryActivity(activity, progress)
            }
        } else if (activity is OnboardingLocationActivity) {

            if (userTypeId == activity.getString(R.string.student_id)) {
                navigateToSelectOOIActivity(activity, progress)
            } else {
                navigateToSelectGenderActivity(activity, progress)
            }
        } else if (activity is OnboardingEthnicityActivity) {

            if (userTypeId == activity.getString(R.string.recruiter_id) || userTypeId == activity.getString(R.string.hiring_manager_id))
                navigateToSelectGroupActivity(activity, progress)
            else
                navigateToSelectBirthdayActivity(activity, progress)
        } else if (activity is SelectGenderActivity) {

            navigateToSelectEthnicityActivity(activity, progress)
        } else if (activity is SelectSchoolActivity) {

            navigateToSelectMajorActivity(activity, progress)
        } else if (activity is SelectMajorActivity) {

            navigateToOnboardingLocationActivity(activity, progress)
        } else if (activity is SelectOccupationActivity) {

            if (userTypeId == activity.getString(R.string.professional_id) || userTypeId == activity.getString(R.string.job_seeker_id))
                navigateToOnboardingLocationActivity(activity, progress)
            else
                navigateToSelectGenderActivity(activity, progress)
        } else if (activity is SelectInterestsActivity) {

            if (userTypeId == activity.getString(R.string.student_id))
                navigateToSelectGroupActivity(activity, progress)
            else
                navigateToSelectGenderActivity(activity, progress)
        } else if (activity is SelectAgeActivity) {

            navigateToSelectGroupActivity(activity, progress)
        } else if (activity is SelectOOIActivity) {

            if (userTypeId == activity.getString(R.string.student_id))
                navigateToSelectGenderActivity(activity, progress)
        } else if (activity is SelectIndustryOnboardingActivity) {
            navigateToOnboardingLocationActivity(activity, progress)
        }
    }
}