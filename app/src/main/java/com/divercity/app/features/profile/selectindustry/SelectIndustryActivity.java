package com.divercity.app.features.profile.selectindustry;

import android.content.Context;
import android.content.Intent;

import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.core.base.BaseFragmentActivity;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectIndustryActivity extends BaseFragmentActivity {

    public static Intent getCallingIntent(Context context){
        return new Intent(context, SelectIndustryActivity.class);
    }

    @Override
    protected BaseFragment fragment() {
        return SelectIndustryFragment.newInstance();
    }

}
