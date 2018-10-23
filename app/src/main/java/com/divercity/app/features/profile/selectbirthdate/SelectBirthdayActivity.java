package com.divercity.app.features.profile.selectbirthdate;

import android.content.Context;
import android.content.Intent;

import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.core.base.BaseFragmentActivity;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectBirthdayActivity extends BaseFragmentActivity {


    public static Intent getCallingIntent(Context context){
        Intent intent = new Intent(context, SelectBirthdayActivity.class);
        return intent;
    }

    @Override
    protected BaseFragment fragment() {
        return SelectBirthdayFragment.newInstance();
    }
}
