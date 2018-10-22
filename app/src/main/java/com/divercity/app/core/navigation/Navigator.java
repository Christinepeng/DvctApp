package com.divercity.app.core.navigation;

import android.content.Context;

import com.divercity.app.features.profile.selectcompany.SelectCompanyActivity;
import com.divercity.app.features.profile.selectindustry.SelectIndustryActivity;
import com.divercity.app.features.profile.selectusertype.SelectUserTypeActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by lucas on 18/10/2018.
 */

@Singleton
public class Navigator {

    @Inject
    public Navigator(){}

    public void navigateToSelectUserType(Context context){
        context.startActivity(SelectUserTypeActivity.getCallingIntent(context));
    }

    public void navigateToSelectCompany(Context context){
        context.startActivity(SelectCompanyActivity.getCallingIntent(context));
    }

    public void navigateToSelectIndustry(Context context){
        context.startActivity(SelectIndustryActivity.getCallingIntent(context));
    }
}
