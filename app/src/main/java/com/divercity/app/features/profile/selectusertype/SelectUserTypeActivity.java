package com.divercity.app.features.profile.selectusertype;

import android.content.Context;
import android.content.Intent;

import com.divercity.app.core.base.BaseFragmentActivity;
import com.divercity.app.core.base.BaseFragment;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectUserTypeActivity extends BaseFragmentActivity {

    public static final String COMPLETE_PROFILE = "completeProfile";

    public static Intent getCallingIntent(Context context, boolean completeProfile){
        Intent intent = new Intent(context,SelectUserTypeActivity.class);
        intent.putExtra(COMPLETE_PROFILE, completeProfile);
        return intent;
    }

    @Override
    protected BaseFragment fragment() {
        return SelectUserTypeFragment.newInstance(getIntent().getBooleanExtra(COMPLETE_PROFILE,false));
    }
}
