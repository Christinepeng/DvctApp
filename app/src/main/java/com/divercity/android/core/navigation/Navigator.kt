package com.divercity.android.core.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.divercity.android.R
import com.divercity.android.core.utils.OnboardingProgression
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.features.agerange.onboarding.OnboardingAgeActivity
import com.divercity.android.features.agerange.withtoolbar.ToolbarAgeActivity
import com.divercity.android.features.chat.chat.ChatActivity
import com.divercity.android.features.chat.creategroupchat.CreateGroupChatActivity
import com.divercity.android.features.chat.newchat.NewChatActivity
import com.divercity.android.features.chat.newgroupchat.NewGroupChatActivity
import com.divercity.android.features.chat.recentchats.ChatsActivity
import com.divercity.android.features.company.companyaddadmin.CompanyAddAdminActivity
import com.divercity.android.features.company.companyadmin.CompanyAdminActivity
import com.divercity.android.features.company.companydetail.CompanyDetailActivity
import com.divercity.android.features.company.companysize.CompanySizesActivity
import com.divercity.android.features.company.createcompany.CreateCompanyActivity
import com.divercity.android.features.company.deleteadmincompany.DeleteCompanyAdminActivity
import com.divercity.android.features.company.mycompanies.MyCompaniesActivity
import com.divercity.android.features.company.ratecompany.RateCompanyActivity
import com.divercity.android.features.company.selectcompany.onboarding.OnboardingCompanyActivity
import com.divercity.android.features.company.selectcompany.withtoolbar.ToolbarCompanyActivity
import com.divercity.android.features.education.addediteducation.AddEditEducationActivity
import com.divercity.android.features.education.degree.SelectDegreeActivity
import com.divercity.android.features.ethnicity.onboarding.OnboardingEthnicityActivity
import com.divercity.android.features.ethnicity.withtoolbar.ToolbarEthnicityActivity
import com.divercity.android.features.gender.onboarding.OnboardingGenderActivity
import com.divercity.android.features.gender.withtoolbar.ToolbarGenderActivity
import com.divercity.android.features.groups.createeditgroup.step1.CreateEditGroupStep1Activity
import com.divercity.android.features.groups.createeditgroup.step3.CreateEditGroupStep3Activity
import com.divercity.android.features.groups.createnewpost.CreateNewPostActivity
import com.divercity.android.features.groups.createtopic.CreateTopicActivity
import com.divercity.android.features.groups.deletegroupadmin.DeleteGroupAdminActivity
import com.divercity.android.features.groups.deletegroupmember.DeleteGroupMemberActivity
import com.divercity.android.features.groups.followedgroups.FollowedGroupsActivity
import com.divercity.android.features.groups.groupanswers.AnswerActivity
import com.divercity.android.features.groups.groupdetail.GroupDetailActivity
import com.divercity.android.features.groups.mygroups.MyGroupsActivity
import com.divercity.android.features.groups.onboarding.SelectGroupActivity
import com.divercity.android.features.groups.selectfollowedgroup.SelectFollowedGroupActivity
import com.divercity.android.features.home.HomeActivity
import com.divercity.android.features.industry.onboarding.SelectIndustryOnboardingActivity
import com.divercity.android.features.industry.selectsingleindustry.SelectSingleIndustryActivity
import com.divercity.android.features.invitations.contacts.InvitePhoneContactsActivity
import com.divercity.android.features.invitations.users.InviteUsersActivity
import com.divercity.android.features.jobs.applicants.JobApplicantsActivity
import com.divercity.android.features.jobs.detail.detail.JobDetailActivity
import com.divercity.android.features.jobs.detail.poster.JobDescriptionPosterActivity
import com.divercity.android.features.jobs.jobposting.JobPostingActivity
import com.divercity.android.features.jobs.jobposting.jobtype.JobTypeActivity
import com.divercity.android.features.jobs.jobposting.sharetogroup.ShareJobGroupActivity
import com.divercity.android.features.linkedin.LinkedinActivity
import com.divercity.android.features.loadurl.LoadUrlActivity
import com.divercity.android.features.location.onboarding.OnboardingLocationActivity
import com.divercity.android.features.location.withtoolbar.ToolbarLocationActivity
import com.divercity.android.features.login.step1.LogInPageActivity
import com.divercity.android.features.login.step1.GetStartedActivity
import com.divercity.android.features.login.step2.LoginActivity
import com.divercity.android.features.major.onboarding.OnboardingMajorActivity
import com.divercity.android.features.major.withtoolbar.SelectSingleMajorActivity
import com.divercity.android.features.multipleuseraction.MultipleUserActionActivity
import com.divercity.android.features.onboarding.profileprompt.ProfilePromptActivity
import com.divercity.android.features.onboarding.selectinterests.SelectInterestsActivity
import com.divercity.android.features.onboarding.selectoccupation.SelectOccupationActivity
import com.divercity.android.features.onboarding.selectoccupationofinterests.SelectOOIActivity
import com.divercity.android.features.onboarding.selectusertype.SelectUserTypeActivity
import com.divercity.android.features.onboarding.uploadresume.UploadResumeActivity
import com.divercity.android.features.password.changepassword.ChangePasswordActivity
import com.divercity.android.features.password.resetpassword.ResetPasswordActivity
import com.divercity.android.features.picturessearch.PictureSearchActivity
import com.divercity.android.features.school.onboarding.OnboardingSchoolActivity
import com.divercity.android.features.school.withtoolbar.SelectSingleSchoolActivity
import com.divercity.android.features.search.SearchActivity
import com.divercity.android.features.settings.ProfileSettingsActivity
import com.divercity.android.features.settings.accountsettings.AccountSettingsActivity
import com.divercity.android.features.signup.SignUpActivity
import com.divercity.android.features.signup.SignUpPageActivity
import com.divercity.android.features.singleuseraction.SingleUserActionActivity
import com.divercity.android.features.skill.editskills.EditUserSkillActivity
import com.divercity.android.features.skill.jobskills.JobSkillsActivity
import com.divercity.android.features.skill.onboarding.OnboardingSkillActivity
import com.divercity.android.features.splash.SplashActivity
import com.divercity.android.features.user.addeditworkexperience.AddEditWorkExperienceActivity
import com.divercity.android.features.user.editexperienceeducation.EditExperienceEducationActivity
import com.divercity.android.features.user.editpersonal.PersonalSettingsActivity
import com.divercity.android.features.user.myinterests.InterestsActivity
import com.divercity.android.features.user.profileotheruser.OtherUserProfileActivity
import com.divercity.android.model.Education
import com.divercity.android.model.Question
import com.divercity.android.model.WorkExperience
import com.divercity.android.model.user.User
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
        val intent = GetStartedActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }

    fun navigateToJobDetail(
        activity: FragmentActivity,
        jobId: String?,
        job: JobResponse?
    ) {
        activity.startActivity(JobDetailActivity.getCallingIntent(activity, jobId, job))
    }

    fun navigateToJobDetailForResult(fragment: Fragment, job: JobResponse, code: Int) {
        fragment.startActivityForResult(
            JobDetailActivity.getCallingIntent(
                fragment.requireContext(),
                job.id,
                job
            ), code
        )
    }

    fun navigateToSignUpActivity(activity: FragmentActivity, email: String) {
        activity.startActivity(SignUpActivity.getCallingIntent(activity, email))
    }

    fun navigateToLoginActivity(activity: FragmentActivity, email: String) {
        activity.startActivity(LoginActivity.getCallingIntent(activity, email))
    }

    fun navigateToEditExperienceEducationForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            EditExperienceEducationActivity.getCallingIntent(fragment.requireActivity()),
            code
        )
    }

    fun navigateToProfilePromptActivity(activity: FragmentActivity) {
        activity.startActivity(ProfilePromptActivity.getCallingIntent(activity))
    }

    fun navigateToSelectUserTypeActivity(activity: FragmentActivity) {
        activity.startActivity(SelectUserTypeActivity.getCallingIntent(activity))
    }

    fun navigateToOnboardingSchool(activity: FragmentActivity, progress: Int) {
        activity.startActivity(OnboardingSchoolActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectSchool(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            SelectSingleSchoolActivity.getCallingIntent(fragment.context),
            code
        )
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

    fun navigateToOnboardingMajor(activity: FragmentActivity, progress: Int) {
        activity.startActivity(OnboardingMajorActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectMajor(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            SelectSingleMajorActivity.getCallingIntent(fragment.context),
            code
        )
    }

    fun navigateToOnboardingSkillActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(OnboardingSkillActivity.getCallingIntent(activity, progress))
    }

    fun navigateToSelectOOIActivity(activity: FragmentActivity, progress: Int) {
        activity.startActivity(SelectOOIActivity.getCallingIntent(activity, progress))
    }

    fun navigateToPhoneContactsActivity(activity: FragmentActivity, data: Bundle) {
        activity.startActivity(InvitePhoneContactsActivity.getCallingIntent(activity, data))
    }

    fun navigateToInviteUsers(activity: FragmentActivity, data: Bundle) {
        activity.startActivity(InviteUsersActivity.getCallingIntent(activity, data))
    }

    fun navigateToLinkedinActivity(activity: FragmentActivity) {
        activity.startActivity(LinkedinActivity.getCallingIntent(activity))
    }

    fun navigateToLogInPageFragment(activity: FragmentActivity) {
        activity.startActivity(LogInPageActivity.getCallingIntent(activity))
    }

    fun navigateToSignUpPageFragment(activity: FragmentActivity) {
        activity.startActivity(SignUpPageActivity.getCallingIntent(activity))
    }

    fun navigateToChatsActivity(activity: FragmentActivity) {
        activity.startActivity(ChatsActivity.getCallingIntent(activity))
    }

    fun navigateToMyCompanies(activity: FragmentActivity) {
        activity.startActivity(MyCompaniesActivity.getCallingIntent(activity))
    }

    fun navigateToNewChatActivity(fragment: Fragment) {
        fragment.startActivity(NewChatActivity.getCallingIntent(fragment.context))
    }

    fun navigateToEditUserSkills(fragment: Fragment, prevSkills: ArrayList<String>?) {
        fragment.startActivity(EditUserSkillActivity.getCallingIntent(fragment.context, prevSkills))
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

    fun navigateToResetPassword(activity: Activity, token: String) {
        activity.startActivity(
            ResetPasswordActivity.getCallingIntent(
                activity,
                token
            )
        )
    }

    fun navigateToSearch(fragment: Fragment, searchQuery: String) {
        fragment.startActivity(
            SearchActivity.getCallingIntent(
                fragment.context,
                searchQuery
            )
        )
    }

    fun navigateToChangePassword(fragment: Fragment) {
        fragment.startActivity(
            ChangePasswordActivity.getCallingIntent(
                fragment.context
            )
        )
    }

    fun navigateToCreateNewPost(fragment: Fragment) {
        fragment.startActivity(
            CreateNewPostActivity.getCallingIntent(
                fragment.context
            )
        )
    }

    fun navigateToSplash(fragment: Fragment) {
        fragment.startActivity(
            SplashActivity.getCallingIntent(fragment.context)
        )
    }

    fun navigateToGroupDetailForResult(fragment: Fragment, group: GroupResponse, code: Int) {
        fragment.startActivityForResult(
            GroupDetailActivity.getCallingIntent(
                fragment.context,
                group.id,
                group
            ), code
        )
    }

    fun navigateToSelectDegree(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            SelectDegreeActivity.getCallingIntent(fragment.context),
            code
        )
    }

    fun navigateToGroupDetail(activity: Activity, groupId: String) {
        activity.startActivity(
            GroupDetailActivity.getCallingIntent(
                activity,
                groupId,
                null
            )
        )
    }


    fun navigateToGroupDetail(fragment: Fragment, group: GroupResponse) {
        fragment.startActivity(
            GroupDetailActivity.getCallingIntent(
                fragment.context,
                group.id,
                group
            )
        )
    }

    fun navigateToCreateGroupStep1(fragment: Fragment) {
        fragment.startActivity(
            CreateEditGroupStep1Activity.getCallingIntent(
                fragment.context,
                null
            )
        )
    }

    fun navigateToEditGroupStep1(fragment: Fragment, group: GroupResponse?) {
        fragment.startActivity(
            CreateEditGroupStep1Activity.getCallingIntent(
                fragment.context,
                group
            )
        )
    }

    fun navigateToCreateGroupStep3(
        fragment: Fragment,
        groupName: String,
        photoPath: String?
    ) {
        fragment.startActivity(
            CreateEditGroupStep3Activity.getCallingIntent
                (fragment.context, groupName, photoPath, null)
        )
    }

    fun navigateToEditGroupStep3(
        fragment: Fragment,
        groupName: String,
        photoPath: String?,
        group: GroupResponse?
    ) {
        fragment.startActivity(
            CreateEditGroupStep3Activity.getCallingIntent
                (fragment.context, groupName, photoPath, group)
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

    fun navigateToPictureSearchActivityForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            PictureSearchActivity.getCallingIntent(fragment.context),
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
            SelectFollowedGroupActivity.getCallingIntent(fragment.context),
            code
        )
    }

    fun navigateToFollowedGroups(fragment: Fragment) {
        fragment.startActivity(
            FollowedGroupsActivity.getCallingIntent(fragment.context)
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

    fun navigateToMyGroups(fragment: Fragment) {
        fragment.startActivity(MyGroupsActivity.getCallingIntent(fragment.context))
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

    fun navigateToInterestsActivity(fragment: Fragment, isEdition: Boolean) {
        fragment.startActivity(InterestsActivity.getCallingIntent(fragment.context, isEdition))
    }

    fun navigateToOtherUserProfileForResult(
        fragment: Fragment,
        userId: String?,
        user: User?,
        code: Int
    ) {
        fragment.startActivityForResult(
            OtherUserProfileActivity.getCallingIntent(
                fragment.context,
                userId,
                user
            ),
            code
        )
    }

    fun navigateToOtherUserProfile(
        fragment: Fragment,
        userId: String?
    ) {
        fragment.startActivity(
            OtherUserProfileActivity.getCallingIntent(
                fragment.context,
                userId,
                null
            )
        )
    }

    fun navigateToLoadUrlActivity(fragment: Fragment, url: String) {
        fragment.startActivity(LoadUrlActivity.getCallingIntent(fragment.context, url))
    }

    fun navigateToAccountSettingsActivity(fragment: Fragment) {
        fragment.startActivity(AccountSettingsActivity.getCallingIntent(fragment.context))
    }

    fun navigateToAddWorkExperienceForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            AddEditWorkExperienceActivity.getCallingIntent(fragment.context, null),
            code
        )
    }

    fun navigateToEditWorkExperienceForResult(
        fragment: Fragment,
        workExperience: WorkExperience,
        code: Int
    ) {
        fragment.startActivityForResult(
            AddEditWorkExperienceActivity.getCallingIntent(fragment.context, workExperience),
            code
        )
    }

    fun navigateToAddEducationForResult(fragment: Fragment, code: Int) {
        fragment.startActivityForResult(
            AddEditEducationActivity.getCallingIntent(fragment.context, null),
            code
        )
    }

    fun navigateToEditEducationForResult(fragment: Fragment, education: Education, code: Int) {
        fragment.startActivityForResult(
            AddEditEducationActivity.getCallingIntent(fragment.context, education),
            code
        )
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

    fun navigateToCompanyDetail(fragment: Fragment, company: CompanyResponse) {
        fragment.startActivity(
            CompanyDetailActivity.getCallingIntent(
                fragment.requireContext(),
                company.id,
                company
            )
        )
    }

    fun navigateToRateCompany(
        fragment: Fragment,
        company: CompanyResponse,
        isEdition: Boolean,
        code: Int
    ) {
        fragment.startActivityForResult(
            RateCompanyActivity.getCallingIntent(
                fragment.requireContext(),
                isEdition,
                company
            ), code
        )
    }


    fun navigateToCompanyDetail(fragment: Fragment, companyId: String) {
        fragment.startActivity(
            CompanyDetailActivity.getCallingIntent(
                fragment.requireContext(),
                companyId,
                null
            )
        )
    }

    fun navigateToCompanyAdmins(fragment: Fragment, companyId: String) {
        fragment.startActivity(
            CompanyAdminActivity.getCallingIntent(
                fragment.requireContext(),
                companyId
            )
        )
    }

//    fun navigateToCompanyAdminAdd(fragment: Fragment, companyId: String) {
//        fragment.startActivity(
//            SingleUserActionActivity.getCallingIntent(
//                fragment.requireContext(),
//                SingleUserActionActivity.getAddGroupAdminBundle(companyId)
//            )
//        )
//    }

    fun navigateToCompanyAddAdminForResult(fragment: Fragment, companyId: String, code: Int) {
        fragment.startActivityForResult(
            CompanyAddAdminActivity.getCallingIntent(
                fragment.requireContext(),
                companyId
            ), code
        )
    }

    fun navigateToShareJobViaMessage(fragment: Fragment, jobId: String) {
        fragment.startActivity(
            SingleUserActionActivity.getCallingIntent(
                fragment.requireContext(),
                SingleUserActionActivity.getShareJobViaMessageBundle(jobId)
            )
        )
    }

    fun navigateToDeleteGroupAdmin(fragment: Fragment, groupId: String, ownerId: String) {
        fragment.startActivity(
            DeleteGroupAdminActivity.getCallingIntent(
                fragment.requireContext(),
                DeleteGroupAdminActivity.getEditGroupAdminBundle(groupId, ownerId)
            )
        )
    }

    fun navigateToDeleteGroupMembers(fragment: Fragment, groupId: String) {
        fragment.startActivity(
            DeleteGroupMemberActivity.getCallingIntent(
                fragment.requireContext(),
                groupId
            )
        )
    }

    fun navigateToDeleteCompanyAdmin(fragment: Fragment, companyId: String, ownerId: String) {
        fragment.startActivity(
            DeleteCompanyAdminActivity.getCallingIntent(
                fragment.requireContext(),
                companyId,
                ownerId
            )
        )
    }

    fun navigateToAddGroupAdmins(fragment: Fragment, groupId: String) {
        fragment.startActivity(
            MultipleUserActionActivity.getCallingIntent(
                fragment.requireContext(),
                MultipleUserActionActivity.getAddGroupAdminBundle(groupId)
            )
        )
    }

    fun navigateToAnswerActivity(fragment: Fragment, question: Question) {
        fragment.startActivity(AnswerActivity.getCallingIntent(fragment.context, question))
    }

    fun navigateToQuestionAndAnswers(fragment: Fragment, questionId: String) {
        fragment.startActivity(AnswerActivity.getCallingIntent(fragment.context, questionId))
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
                        navigateToOnboardingSkillActivity(activity, progress)
                    is OnboardingSkillActivity ->
                        navigateToOnboardingGenderActivity(activity, progress)
                    is OnboardingGenderActivity ->
                        navigateToOnboardingEthnicityActivity(activity, progress)
                    is OnboardingEthnicityActivity ->
                        navigateToSelectBirthdayActivity(activity, progress)
                    is OnboardingAgeActivity ->
                        navigateToSelectGroupActivity(activity, progress)
                }
            }
            activity.getString(R.string.student_id) -> {
                when (activity) {
                    is ProfilePromptActivity ->
                        navigateToOnboardingSchool(activity, progress)
                    is OnboardingSchoolActivity ->
                        navigateToOnboardingMajor(activity, progress)
                    is OnboardingMajorActivity ->
                        navigateToOnboardingLocationActivity(activity, progress)
                    is OnboardingLocationActivity ->
                        navigateToSelectOOIActivity(activity, progress)
                    is SelectOOIActivity ->
                        navigateToOnboardingSkillActivity(activity, progress)
                    is OnboardingSkillActivity ->
                        navigateToOnboardingGenderActivity(activity, progress)
                    is OnboardingGenderActivity ->
                        navigateToOnboardingEthnicityActivity(activity, progress)
                    is OnboardingEthnicityActivity ->
                        navigateToSelectBirthdayActivity(activity, progress)
                    is OnboardingAgeActivity ->
                        navigateToSelectGroupActivity(activity, progress)
                }
            }
        }
    }
}