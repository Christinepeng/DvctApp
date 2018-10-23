package com.divercity.app.features.profile.selectgroups;

import android.content.Context;
import android.content.Intent;

import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.core.base.BaseFragmentActivity;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectGroupActivity extends BaseFragmentActivity {

    public static Intent getCallingIntent(Context context){
        return new Intent(context, SelectGroupActivity.class);
    }

    @Override
    protected BaseFragment fragment() {
        return SelectGroupFragment.newInstance();
    }
}
