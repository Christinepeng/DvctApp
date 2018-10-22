package com.divercity.app.features.profile.selectusertype;

import android.content.Context;
import android.content.Intent;

import com.divercity.app.core.base.BaseFragmentActivity;
import com.divercity.app.core.base.BaseFragment;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectUserTypeActivity extends BaseFragmentActivity {

    public static Intent getCallingIntent(Context context){
        return new Intent(context,SelectUserTypeActivity.class);
    }

    @Override
    protected BaseFragment fragment() {
        return SelectUserTypeFragment.newInstance();
    }

}
