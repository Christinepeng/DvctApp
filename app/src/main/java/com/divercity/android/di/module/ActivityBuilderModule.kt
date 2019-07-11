package com.divercity.android.di.module

import com.divercity.android.di.module.fragments.FragmentBuilderModule
import com.divercity.android.di.module.fragments.FragmentDataBuilderModule
import com.divercity.android.di.module.fragments.FragmentSessionBuilderModule
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
import com.divercity.android.features.login.step1.EnterEmailActivity
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
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by lucas on 25/10/2018.
 */

@Module
abstract class ActivityBuilderModule {

    //COMPANY

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindOnboardingCompanyActivity(): OnboardingCompanyActivity

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindToolbarCompanyActivity(): ToolbarCompanyActivity

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindCreateCompanyActivity(): CreateCompanyActivity

    //SCHOOL

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindOnboardingSchoolActivity(): OnboardingSchoolActivity

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindToolbarSchoolActivity(): SelectSingleSchoolActivity

    //LOCATION

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindOnboardingLocationActivity(): OnboardingLocationActivity

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindSelectLocationActivity(): ToolbarLocationActivity

    //MAJOR

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindOnboardingMajorActivity(): OnboardingMajorActivity

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindSelectSingleMajorActivity(): SelectSingleMajorActivity

    //INDUSTRY

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindOnboardingIndustryActivity(): SelectIndustryOnboardingActivity

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindToolbarIndustryActivity(): SelectSingleIndustryActivity

    //ETHNICITY

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindOnboardingEthnicityActivity(): OnboardingEthnicityActivity


    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindToolbarEthnicityActivity(): ToolbarEthnicityActivity

    //GENDER

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindOnboardingGenderActivity(): OnboardingGenderActivity

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindToolbarGenderActivity(): ToolbarGenderActivity

    //AGE

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindToolbarAgeActivity(): ToolbarAgeActivity

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindSelectBirthdayActivity(): OnboardingAgeActivity

    //SKILL

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindOnboardingSkillActivity(): OnboardingSkillActivity

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindJobSkillsActivity(): JobSkillsActivity

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindEditUserSkillActivity(): EditUserSkillActivity

    //DEGREE

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindSelectDegreeActivity(): SelectDegreeActivity

    //PICTURES

    @ContributesAndroidInjector(modules = [FragmentDataBuilderModule::class])
    abstract fun bindPictureSearchActivity(): PictureSearchActivity

    //SESSION

    @ContributesAndroidInjector(modules = [FragmentSessionBuilderModule::class])
    abstract fun bindEnterEmailActivity(): EnterEmailActivity

    @ContributesAndroidInjector(modules = [FragmentSessionBuilderModule::class])
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [FragmentSessionBuilderModule::class])
    abstract fun bindSignUpActivity(): SignUpActivity


    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindHomeActivity(): HomeActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectUserTypeActivity(): SelectUserTypeActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindProfilePromptActivity(): ProfilePromptActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectGroupActivity(): SelectGroupActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectInterestsActivity(): SelectInterestsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectOccupationActivity(): SelectOccupationActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindLinkedinActivity(): LinkedinActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindJobPostingActivity(): JobPostingActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindJobTypeActivity(): JobTypeActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindShareJobGroupActivity(): ShareJobGroupActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindJobDescriptionSeekerActivity(): JobDetailActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindJobDescriptionPosterActivity(): JobDescriptionPosterActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindJobApplicantsActivity(): JobApplicantsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindCompanySizesActivity(): CompanySizesActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSelectOOIActivity(): SelectOOIActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindDirectMessagesActivity(): ChatsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindNewChatActivity(): NewChatActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindGroupDetailActivity(): GroupDetailActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindChatActivity(): ChatActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindCreateGroupActivity(): CreateEditGroupStep1Activity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindGroupDescriptionActivity(): CreateEditGroupStep3Activity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindProfileSettingsActivity(): ProfileSettingsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class, FragmentDataBuilderModule::class])
    abstract fun bindPersonalSettingsActivity(): PersonalSettingsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindInterestsActivity(): InterestsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindNewGroupChatActivity(): NewGroupChatActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindCreateGroupChatActivity(): CreateGroupChatActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindAccountSettingsActivity(): AccountSettingsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindPhoneContactsActivity(): InvitePhoneContactsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindCreateTopicActivity(): CreateTopicActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindFollowingGroupsActivity(): SelectFollowedGroupActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindAnswerActivity(): AnswerActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindLoadUrlActivity(): LoadUrlActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindUploadResumeActivity(): UploadResumeActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindOtherUserProfileActivity(): OtherUserProfileActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindInviteUsersActivity(): InviteUsersActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindMyCompaniesActivity(): MyCompaniesActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindMyGroupsActivity(): MyGroupsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindCompanyDetailActivity(): CompanyDetailActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindCompanyAdminActivity(): CompanyAdminActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindCompanyAdminAddActivity(): SingleUserActionActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindAddWorkExperienceActivity(): AddEditWorkExperienceActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindCompanyAddAdminActivity(): CompanyAddAdminActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindMultipleUserActionActivity(): MultipleUserActionActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindDeleteGroupAdminActivity(): DeleteGroupAdminActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindDeleteCompanyAdminActivity(): DeleteCompanyAdminActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindRateCompanyActivity(): RateCompanyActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindResetPasswordActivity(): ResetPasswordActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindChangePasswordActivity(): ChangePasswordActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindSearchActivity(): SearchActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindCreateNewPostActivity(): CreateNewPostActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindDeleteGroupMemberActivity(): DeleteGroupMemberActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindFollowedGroupsActivity(): FollowedGroupsActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindEditExperienceEducationActivity(): EditExperienceEducationActivity

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun bindAddEducationActivity(): AddEditEducationActivity
}