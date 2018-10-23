package com.divercity.app.features.profile.profileprompt;

import android.content.Context;
import android.content.Intent;

import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.core.base.BaseFragmentActivity;

/**
 * Created by lucas on 17/10/2018.
 */

public class ProfilePromptActivity extends BaseFragmentActivity {

    public static Intent getCallingIntent(Context context){
        return new Intent(context, ProfilePromptActivity.class);
    }

    @Override
    protected BaseFragment fragment() {
        return ProfilePromptFragment.newInstance();
    }
}
