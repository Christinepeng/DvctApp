package com.divercity.android.core.navigation

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.divercity.android.R
import com.divercity.android.core.utils.OnboardingProgression
import com.divercity.android.data.entity.group.GroupResponse
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.features.agerange.onboarding.OnboardingAgeActivity
import com.divercity.android.features.agerange.withtoolbar.ToolbarAgeActivity
import com.divercity.android.features.chat.chat.ChatActivity
import com.divercity.android.features.chat.creategroupchat.CreateGroupChatActivity
import com.divercity.android.features.chat.newchat.NewChatActivity
import com.divercity.android.features.chat.newgroupchat.NewGroupChatActivity
import com.divercity.android.features.chat.recentchats.ChatsActivity
import com.divercity.android.features.company.companysize.CompanySizesActivity
import com.divercity.android.features.company.createcompany.CreateCompanyActivity
import com.divercity.android.features.company.onboarding.OnboardingCompanyActivity
import com.divercity.android.features.company.withtoolbar.ToolbarCompanyActivity
import com.divercity.android.features.contacts.InvitePhoneContactsActivity
import com.divercity.android.features.ethnicity.onboarding.OnboardingEthnicityActivity
import com.divercity.android.features.ethnicity.withtoolbar.ToolbarEthnicityActivity
import com.divercity.android.features.gender.onboarding.OnboardingGenderActivity
import com.divercity.android.features.gender.withtoolbar.ToolbarGenderActivity
import com.divercity.android.features.groups.answers.AnswerActivity
import com.divercity.android.features.groups.answers.model.Question
import com.divercity.android.features.groups.creategroup.step1.CreateGroupActivity
import com.divercity.android.features.groups.creategroup.step3.GroupDescriptionActivity
import com.divercity.android.features.groups.createtopic.CreateTopicActivity
import com.divercity.android.features.groups.followedgroups.FollowingGroupsActivity
import com.divercity.android.features.groups.groupdetail.GroupDetailActivity
import com.divercity.android.features.groups.onboarding.SelectGroupActivity
import com.divercity.android.features.home.HomeActivity
import com.divercity.android.features.industry.onboarding.SelectIndustryOnboardingActivity
import com.divercity.android.features.industry.selectsingleindustry.SelectSingleIndustryActivity
import com.divercity.android.features.jobs.applicants.JobApplicantsActivity
import com.divercity.android.features.jobs.description.detail.JobDetailActivity
import com.divercity.android.features.jobs.description.poster.JobDescriptionPosterActivity
import com.divercity.android.features.jobs.jobposting.JobPostingActivity
import com.divercity.android.features.jobs.jobposting.jobtype.JobTypeActivity
import com.divercity.android.features.jobs.jobposting.sharetogroup.ShareJobGroupActivity
import com.divercity.android.features.jobs.jobposting.skills.JobSkillsActivity
import com.divercity.android.features.linkedin.LinkedinActivity
import com.divercity.android.features.loadurl.LoadUrlActivity
import com.divercity.android.features.location.onboarding.OnboardingLocationActivity
import com.divercity.android.features.location.withtoolbar.ToolbarLocationActivity
import com.divercity.android.features.login.step1.EnterEmailActivity
import com.divercity.android.features.login.step2.LoginActivity
import com.divercity.android.features.onboarding.profileprompt.ProfilePromptActivity
import com.divercity.android.features.onboarding.selectinterests.SelectInterestsActivity
import com.divercity.android.features.onboarding.selectmajor.SelectMajorActivity
import com.divercity.android.features.onboarding.selectoccupation.SelectOccupationActivity
import com.divercity.android.features.onboarding.selectoccupationofinterests.SelectOOIActivity
import com.divercity.android.features.onboarding.selectschool.SelectSchoolActivity
import com.divercity.android.features.onboarding.selectskill.SelectSkillActivity
import com.divercity.android.features.onboarding.selectusertype.SelectUserTypeActivity
import com.divercity.android.features.onboarding.uploadresume.UploadResumeActivity
import com.divercity.android.features.profile.settings.ProfileSettingsActivity
import com.divercity.android.features.profile.settings.accountsettings.AccountSettingsActivity
import com.divercity.android.features.profile.settings.interests.InterestsActivity
import com.divercity.android.features.profile.settings.personalsettings.PersonalSettingsActivity
import com.divercity.android.features.signup.SignUpActivity
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

    fun navigateToHomeActivityAndClearTop(fragment: Fragment) {
        val intent = HomeActivity.getCallingIntent(fragment.context, false)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        fragment.startActivity(intent)
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

    fun navigateToUploadResumeActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(UploadResumeActivity.getCallingIntent(activity, progress))
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
        activity.startActivity(
            SelectIndustryOnboardingActivity.getCallingIntent(
                activity,
                progress
            )
        )
    }

    fun navigateToOnboardingGenderActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(OnboardingGenderActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectInterestsActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectInterestsActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectBirthdayActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(OnboardingAgeActivity.getCallingIntent(activity, progress))
    }

    fun navigateToOnboardingEthnicityActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(OnboardingEthnicityActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectMajorActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectMajorActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectSkillActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectSkillActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectOOIActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectOOIActivity.getCallingIntent(activity, progress))
    }

    fun navigateToPhoneContactsActivity(activity: FragmentActivity, data: Bundle) {
        activity.startActivity(InvitePhoneContactsActivity.getCallingIntent(activity, data))
    }

    fun navigateToLinkedinActivity(activity: FragmentActivity) {
        activity.startActivity(LinkedinActivity.getCallingIntent(activity))
    }

    fun navigateToChatsActivity(activity: FragmentActivity) {
        activity.startActivity(ChatsActivity.getCallingIntent(activity))
    }

    fun navigateToNewChatActivity(fragment: Fragment) {
        fragment.startActivity(NewChatActivity.getCallingIntent(fragment.context))
    }

    fun navigateToChatActivity(fragment: Fragment, userName: String, userId: String, chatId: Int?) {
        fragment.startActivity(
            ChatActivity.getCallingIntent(
                fragment.context,
                userName,
                userId,
                chatId
            )
        )
    }

    fun navigateToGroupDetailActivity(fragment: Fragment, group: GroupResponse) {
        fragment.startActivity(GroupDetailActivity.getCallingIntent(fragment.context, group))
    }

    fun navigateToCreateGroupActivity(fragment: Fragment) {
        fragment.startActivity(CreateGroupActivity.getCallingIntent(fragment.context))
    }

    fun navigateToGroupDescriptionActivity(
        fragment: Fragment,
        groupName: String,
        photoPath: String?
    ) {
        fragment.startActivity(
            GroupDescriptionActivity.getCallingIntent
                (fragment.context, groupName, photoPath)
        )
    }

    fun navigateToJobPostingForResultActivity(fragment: Fragment, code: Int, job: JobResponse?) {
        fragment.startActivityForResult(
            JobPostingActivity.getCallingIntent(fragment.context, job),
            code
        )
    }

    fun navigateToCreateCompanyActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            CreateCompanyActivity.getCallingIntent(fragment.context),
            code
        )
    }

    fun navigateToToolbarLocationActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            ToolbarLocationActivity.getCallingIntent(fragment.context),
            code
        )
    }

    fun navigateToProfileSettingsActivity(fragment: Fragment) {
        fragment.startActivity(ProfileSettingsActivity.getCallingIntent(fragment.context))
    }

    fun navigateToSelectSingleIndustryActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            SelectSingleIndustryActivity.getCallingIntent(fragment.context),
            code
        )
    }

    fun navigateToToolbarCompanyActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            ToolbarCompanyActivity.getCallingIntent(fragment.context),
            code
        )
    }

    fun navigateToCompanySizesActivity(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            CompanySizesActivity.getCallingIntent(fragment.context),
            code
        )
    }

    fun navigateToJobTypeActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(JobTypeActivity.getCallingIntent(fragment.context), code)
    }

    fun navigateToJobSkillsActivityForResult(
        fragment: Fragment,
        code: Int,
        skillList: ArrayList<SkillResponse>
    ) {
        fragment.startActivityForResult(
            JobSkillsActivity.getCallingIntent(
                fragment.context,
                skillList
            ), code
        )
    }

    fun navigateToToolbarEthnicityActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            ToolbarEthnicityActivity.getCallingIntent(fragment.context),
            code
        )
    }

    fun navigateToFollowingGroupsActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            FollowingGroupsActivity.getCallingIntent(fragment.context),
            code
        )
    }

    fun navigateToToolbarAgeActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(ToolbarAgeActivity.getCallingIntent(fragment.context), code)
    }

    fun navigateToToolbarGenderActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            ToolbarGenderActivity.getCallingIntent(fragment.context),
            code
        )
    }

    fun navigateToShareJobGroupActivity(fragment: Fragment, jobId: String?) {
        fragment.startActivity(ShareJobGroupActivity.getCallingIntent(fragment.context, jobId))
    }

    fun navigateToJobDescriptionPosterForResultActivity(
        fragment: Fragment,
        code: Int,
        job: JobResponse
    ) {
        fragment.startActivityForResult(
            JobDescriptionPosterActivity.getCallingIntent(
                fragment.context,
                job
            ), code
        )
    }

    fun navigateToJobApplicantsActivity(fragment: Fragment, job: JobResponse) {
        fragment.startActivity(JobApplicantsActivity.getCallingIntent(fragment.context, job))
    }

    fun navigateToPersonalSettingsActivity(fragment: Fragment) {
        fragment.startActivity(PersonalSettingsActivity.getCallingIntent(fragment.context))
    }

    fun navigateToInterestsActivity(fragment: Fragment) {
        fragment.startActivity(InterestsActivity.getCallingIntent(fragment.context))
    }

    fun navigateToLoadUrlActivity(fragment: Fragment, url: String) {
        fragment.startActivity(LoadUrlActivity.getCallingIntent(fragment.context, url))
    }

    fun navigateToAccountSettingsActivity(fragment: Fragment) {
        fragment.startActivity(AccountSettingsActivity.getCallingIntent(fragment.context))
    }

    fun navigateToNewGroupChatActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            NewGroupChatActivity.getCallingIntent(fragment.context),
            code
        )
    }

    fun navigateToCreateGroupChatActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            CreateGroupChatActivity.getCallingIntent(fragment.context),
            code
        )
    }

    fun navigateToCreateTopicActivity(fragment: Fragment, group: GroupResponse?) {
        fragment.startActivity(CreateTopicActivity.getCallingIntent(fragment.context, group))
    }

    fun navigateToAnswerActivity(fragment: Fragment, question: Question) {
        fragment.startActivity(AnswerActivity.getCallingIntent(fragment.context, question))
    }

    fun navigateToNextOnboarding(
        activity: FragmentActivity,
        userTypeId: String,
        cprogress: Int,
        shouldIncrementProgress: Boolean
    ) {
        var progress = cprogress
        if (shouldIncrementProgress) {
            progress = OnboardingProgression.getNextNavigationProgressOnboarding(
                activity,
                userTypeId,
                progress
            )
        }

        when (userTypeId) {
            activity.getString(R.string.recruiter_id) -> {
                when (activity) {
                    is ProfilePromptActivity ->
                        navigateToSelectCompanyActivity(activity, progress)
                    is OnboardingCompanyActivity ->
                        navigateToOnboardingIndustryActivity(activity, progress)
                    is SelectIndustryOnboardingActivity ->
                        navigateToOnboardingLocationActivity(activity, progress)
                    is OnboardingLocationActivity ->
                        navigateToOnboardingGenderActivity(activity, progress)
                    is OnboardingGenderActivity ->
                        navigateToOnboardingEthnicityActivity(activity, progress)
                    is OnboardingEthnicityActivity ->
                        navigateToSelectGroupActivity(activity, progress)
                }
            }
            activity.getString(R.string.hiring_manager_id) -> {
                when (activity) {
                    is ProfilePromptActivity ->
                        navigateToSelectCompanyActivity(activity, progress)
                    is OnboardingCompanyActivity ->
                        navigateToOnboardingIndustryActivity(activity, progress)
                    is SelectIndustryOnboardingActivity ->
                        navigateToSelectOccupationActivity(activity, progress)
                    is SelectOccupationActivity ->
                        navigateToOnboardingLocationActivity(activity, progress)
                    is OnboardingLocationActivity ->
                        navigateToOnboardingGenderActivity(activity, progress)
                    is OnboardingGenderActivity ->
                        navigateToOnboardingEthnicityActivity(activity, progress)
                    is OnboardingEthnicityActivity ->
                        navigateToSelectGroupActivity(activity, progress)
                }
            }
            activity.getString(R.string.professional_id),
            activity.getString(R.string.entrepreneur_id),
            activity.getString(R.string.job_seeker_id) -> {
                when (activity) {
                    is ProfilePromptActivity ->
                        navigateToSelectCompanyActivity(activity, progress)
                    is OnboardingCompanyActivity ->
                        navigateToOnboardingIndustryActivity(activity, progress)
                    is SelectIndustryOnboardingActivity ->
                        navigateToSelectOccupationActivity(activity, progress)
                    is SelectOccupationActivity ->
                        navigateToOnboardingLocationActivity(activity, progress)
                    is OnboardingLocationActivity ->
                        navigateToUploadResumeActivity(activity, progress)
                    is UploadResumeActivity ->
                        navigateToSelectSkillActivity(activity, progress)
                    is SelectSkillActivity ->
                        navigateToOnboardingGenderActivity(activity, progress)
                    is OnboardingEthnicityActivity ->
                        navigateToSelectBirthdayActivity(activity, progress)
                    is OnboardingAgeActivity ->
                        navigateToSelectGroupActivity(activity, progress)
                }
            }
            activity.getString(R.string.student_id) -> {
                when (activity) {
                    is ProfilePromptActivity ->
                        navigateToSelectSchoolActivity(activity, progress)
                    is SelectSchoolActivity ->
                        navigateToSelectMajorActivity(activity, progress)
                    is SelectMajorActivity ->
                        navigateToOnboardingLocationActivity(activity, progress)
                    is OnboardingLocationActivity ->
                        navigateToSelectOOIActivity(activity, progress)
                    is SelectOOIActivity ->
                        navigateToSelectSkillActivity(activity, progress)
                    is SelectSkillActivity ->
                        navigateToOnboardingGenderActivity(activity, progress)
                    is OnboardingEthnicityActivity ->
                        navigateToSelectBirthdayActivity(activity, progress)
                    is OnboardingAgeActivity ->
                        navigateToSelectGroupActivity(activity, progress)
                }
            }
        }

//        if (activity is ProfilePromptActivity) {
//            if (userTypeId == activity.getString(R.string.student_id))
//                navigateToSelectSchoolActivity(activity, progress)
//            else
//                navigateToSelectCompanyActivity(activity, progress)
//        } else if (activity is OnboardingCompanyActivity) {
//
//            if (userTypeId == activity.getString(R.string.recruiter_id) || userTypeId == activity.getString(
//                    R.string.hiring_manager_id
//                )
//            )
//                navigateToOnboardingLocationActivity(activity, progress)
//            else if (userTypeId == activity.getString(R.string.professional_id) || userTypeId == activity.getString(
//                    R.string.job_seeker_id
//                )
//            )
//                navigateToSelectOccupationActivity(activity, progress)
//            else if (userTypeId == activity.getString(R.string.entrepreneur_id)) {
//                navigateToOnboardingIndustryActivity(activity, progress)
//            }
//        } else if (activity is OnboardingLocationActivity) {
//
//            if (userTypeId == activity.getString(R.string.student_id)) {
//                navigateToSelectOOIActivity(activity, progress)
//            } else {
//                navigateToOnboardingGenderActivity(activity, progress)
//            }
//        } else if (activity is OnboardingEthnicityActivity) {
//
//            if (userTypeId == activity.getString(R.string.recruiter_id) || userTypeId == activity.getString(
//                    R.string.hiring_manager_id
//                )
//            )
//                navigateToSelectGroupActivity(activity, progress)
//            else
//                navigateToSelectBirthdayActivity(activity, progress)
//        } else if (activity is OnboardingGenderActivity) {
//
//            navigateToOnboardingEthnicityActivity(activity, progress)
//        } else if (activity is SelectSchoolActivity) {
//
//            navigateToSelectMajorActivity(activity, progress)
//        } else if (activity is SelectMajorActivity) {
//
//            navigateToOnboardingLocationActivity(activity, progress)
//        } else if (activity is SelectOccupationActivity) {
//
//            if (userTypeId == activity.getString(R.string.professional_id) || userTypeId == activity.getString(
//                    R.string.job_seeker_id
//                )
//            )
//                navigateToOnboardingLocationActivity(activity, progress)
//            else
//                navigateToOnboardingGenderActivity(activity, progress)
//        } else if (activity is SelectInterestsActivity) {
//
//            if (userTypeId == activity.getString(R.string.student_id))
//                navigateToSelectGroupActivity(activity, progress)
//            else
//                navigateToOnboardingGenderActivity(activity, progress)
//        } else if (activity is OnboardingAgeActivity) {
//
//            navigateToSelectGroupActivity(activity, progress)
//        } else if (activity is SelectOOIActivity) {
//
//            if (userTypeId == activity.getString(R.string.student_id))
//                navigateToOnboardingGenderActivity(activity, progress)
//        } else if (activity is SelectIndustryOnboardingActivity) {
//            navigateToOnboardingLocationActivity(activity, progress)
//        }
    }
}