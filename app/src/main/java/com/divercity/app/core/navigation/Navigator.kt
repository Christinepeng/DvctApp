package com.divercity.app.core.navigation

import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.divercity.app.R
import com.divercity.app.core.utils.OnboardingProgression
import com.divercity.app.features.home.HomeActivity
import com.divercity.app.features.linkedin.LinkedinActivity
import com.divercity.app.features.login.step1.EnterEmailActivity
import com.divercity.app.features.login.step2.LoginActivity
import com.divercity.app.features.onboarding.profileprompt.ProfilePromptActivity
import com.divercity.app.features.onboarding.selectbirthdate.SelectBirthdayActivity
import com.divercity.app.features.onboarding.selectcompany.SelectCompanyActivity
import com.divercity.app.features.onboarding.selectcountry.SelectCountryActivity
import com.divercity.app.features.onboarding.selectethnicity.SelectEthnicityActivity
import com.divercity.app.features.onboarding.selectgender.SelectGenderActivity
import com.divercity.app.features.onboarding.selectgroups.SelectGroupActivity
import com.divercity.app.features.onboarding.selectindustry.SelectIndustryActivity
import com.divercity.app.features.onboarding.selectinterests.SelectInterestsActivity
import com.divercity.app.features.onboarding.selectmajor.SelectMajorActivity
import com.divercity.app.features.onboarding.selectoccupation.SelectOccupationActivity
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

    fun navigateToSignUpActivity(activity: FragmentActivity, email: String) {
        activity.startActivity(SignUpActivity.getCallingIntent(activity, email))
    }

    fun navigateSelectUserTypeActivity(activity: FragmentActivity) {
        activity.startActivity(SelectUserTypeActivity.getCallingIntent(activity))
    }

    fun navigateProfilePromptActivity(activity: FragmentActivity) {
        activity.startActivity(ProfilePromptActivity.getCallingIntent(activity))
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
        activity.startActivity(SelectCompanyActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectCountryActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectCountryActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectOccupationActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectOccupationActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectIndustryActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectIndustryActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectGenderActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectGenderActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectInterestsActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectInterestsActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectBirthdayActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectBirthdayActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectEthnicityActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectEthnicityActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectMajorActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectMajorActivity.getCallingIntent(activity, progress))
    }

    fun navigateToLinkedinActivity(activity: FragmentActivity) {
        activity.startActivity(LinkedinActivity.getCallingIntent(activity))
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
        } else if (activity is SelectCompanyActivity) {

            if (userTypeId == activity.getString(R.string.recruiter_id) || userTypeId == activity.getString(R.string.hiring_manager_id))
                navigateToSelectCountryActivity(activity, progress)
            else if (userTypeId == activity.getString(R.string.professional_id) || userTypeId == activity.getString(R.string.job_seeker_id))
                navigateToSelectOccupationActivity(activity, progress)
            else if (userTypeId == activity.getString(R.string.entrepreneur_id)) {
                navigateToSelectIndustryActivity(activity, progress)
            }
        } else if (activity is SelectCountryActivity) {

            if (userTypeId == activity.getString(R.string.recruiter_id) || userTypeId == activity.getString(R.string.hiring_manager_id))
                navigateToSelectGenderActivity(activity, progress)
            else if (userTypeId == activity.getString(R.string.student_id)) {
                navigateToSelectOccupationActivity(activity, progress)
            } else {
                navigateToSelectInterestsActivity(activity, progress)
            }
        } else if (activity is SelectEthnicityActivity) {

            if (userTypeId == activity.getString(R.string.recruiter_id) || userTypeId == activity.getString(R.string.hiring_manager_id))
                navigateToSelectGroupActivity(activity, progress)
            else
                navigateToSelectBirthdayActivity(activity, progress)
        } else if (activity is SelectGenderActivity) {

            navigateToSelectEthnicityActivity(activity, progress)
        } else if (activity is SelectSchoolActivity) {

            navigateToSelectMajorActivity(activity, progress)
        } else if (activity is SelectMajorActivity) {
            navigateToSelectCountryActivity(activity, progress)
        } else if (activity is SelectOccupationActivity) {

            if (userTypeId == activity.getString(R.string.professional_id) || userTypeId == activity.getString(R.string.job_seeker_id))
                navigateToSelectCountryActivity(activity, progress)
            else
                navigateToSelectGenderActivity(activity, progress)
        } else if (activity is SelectInterestsActivity) {

            if (userTypeId == activity.getString(R.string.student_id))
                navigateToSelectGroupActivity(activity, progress)
            else
                navigateToSelectGenderActivity(activity, progress)
        } else if (activity is SelectBirthdayActivity) {

            if (userTypeId == activity.getString(R.string.student_id))
                navigateToSelectInterestsActivity(activity, progress)
            else
                navigateToSelectGroupActivity(activity, progress)
        }
    }
}