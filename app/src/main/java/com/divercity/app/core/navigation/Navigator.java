package com.divercity.app.core.navigation;

import android.app.Activity;
import android.content.Context;

import com.divercity.app.R;
import com.divercity.app.features.home.HomeActivity;
import com.divercity.app.features.profile.profileprompt.ProfilePromptActivity;
import com.divercity.app.features.profile.selectbirthdate.SelectBirthdayActivity;
import com.divercity.app.features.profile.selectcompany.SelectCompanyActivity;
import com.divercity.app.features.profile.selectcountry.SelectCountryActivity;
import com.divercity.app.features.profile.selectethnicity.SelectEthnicityActivity;
import com.divercity.app.features.profile.selectgender.SelectGenderActivity;
import com.divercity.app.features.profile.selectgroups.SelectGroupActivity;
import com.divercity.app.features.profile.selectindustry.SelectIndustryActivity;
import com.divercity.app.features.profile.selectschool.SelectSchoolActivity;
import com.divercity.app.features.profile.selectusertype.SelectUserTypeActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by lucas on 18/10/2018.
 */

@Singleton
public class Navigator {

    @Inject
    public Navigator() {
    }

    public void navigateToHome(Context context) {
        context.startActivity(HomeActivity.getCallingIntent(context));
    }

    public void navigateToPromptProfile(Context context) {
        context.startActivity(ProfilePromptActivity.getCallingIntent(context));
    }

    public void navigateToSelectUserType(Context context, boolean completeProfile) {
        context.startActivity(SelectUserTypeActivity.getCallingIntent(context, completeProfile));
    }

    public void navigateToSelectCompany(Context context) {
        context.startActivity(SelectCompanyActivity.getCallingIntent(context));
    }

    public void navigateToSelectIndustry(Context context) {
        context.startActivity(SelectIndustryActivity.getCallingIntent(context));
    }

    public void navigateToSelectSchool(Context context) {
    }

    public void navigateToSelectLocation(Context context) {
        context.startActivity(SelectCountryActivity.getCallingIntent(context));
    }

    public void navigateToSelectGender(Context context) {
        context.startActivity(SelectGenderActivity.getCallingIntent(context));
    }

    public void navigateToSelectEthnicity(Context context) {
        context.startActivity(SelectEthnicityActivity.getCallingIntent(context));
    }

    public void navigateToSelectGroups(Context context) {
        context.startActivity(SelectGroupActivity.getCallingIntent(context));
    }

    public void navigateToOccupation(Context context) {
    }

    public void navigateToSelectInterests(Context context) {
    }

    public void navigateToSelectBirthday(Context context) {
        context.startActivity(SelectBirthdayActivity.getCallingIntent(context));
    }

    public void navigateToSelectMajor(Context context) {
    }

    public void navigateToNextProfile(Activity activity, String userTypeId) {
        if (activity instanceof SelectUserTypeActivity) {

            if (userTypeId.equals(activity.getString(R.string.student_id)))
                navigateToSelectSchool(activity);
            else
                navigateToSelectCompany(activity);
        } else if (activity instanceof SelectCompanyActivity) {

            if (userTypeId.equals(activity.getString(R.string.recruiter_id)) ||
                    userTypeId.equals(activity.getString(R.string.hiring_manager_id)))
                navigateToSelectLocation(activity);
            else if (userTypeId.equals(activity.getString(R.string.professional_id)) ||
                    userTypeId.equals(activity.getString(R.string.job_seeker_id)))
                navigateToOccupation(activity);
            else if (userTypeId.equals(activity.getString(R.string.entrepreneur_id))) {
                navigateToSelectIndustry(activity);
            }
        } else if (activity instanceof SelectCountryActivity) {

            if (userTypeId.equals(activity.getString(R.string.recruiter_id)) ||
                    userTypeId.equals(activity.getString(R.string.hiring_manager_id)))
                navigateToSelectGender(activity);
            else if (userTypeId.equals(activity.getString(R.string.student_id))) {
                navigateToOccupation(activity);
            } else {
                navigateToSelectInterests(activity);
            }
        } else if (activity instanceof SelectEthnicityActivity) {

            if (userTypeId.equals(activity.getString(R.string.recruiter_id)) ||
                    userTypeId.equals(activity.getString(R.string.hiring_manager_id)))
                navigateToSelectGroups(activity);
            else
                navigateToSelectBirthday(activity);
        } else if (activity instanceof SelectGenderActivity) {

            navigateToSelectEthnicity(activity);
        } else if(activity instanceof SelectGroupActivity){

            navigateToHome(activity);
        } else if(activity instanceof SelectSchoolActivity){

            navigateToSelectMajor(activity);
        }

    }
}
