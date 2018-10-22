package com.divercity.app.features.profile.selectoccupation;

import android.content.Context;
import android.content.Intent;

import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.core.base.BaseFragmentActivity;
import com.divercity.app.features.profile.selectusertype.SelectUserTypeFragment;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectCompanyActivity extends BaseFragmentActivity {

    public static Intent getCallingIntent(Context context){
        return new Intent(context, SelectCompanyActivity.class);
    }

    @Override
    protected BaseFragment fragment() {
        return SelectUserTypeFragment.newInstance();
    }

}
