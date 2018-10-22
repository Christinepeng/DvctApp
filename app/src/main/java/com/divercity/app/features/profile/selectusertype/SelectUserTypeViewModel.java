package com.divercity.app.features.profile.selectusertype;

import android.app.Application;
import android.support.annotation.NonNull;

import com.divercity.app.core.base.BaseViewModel;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectUserTypeViewModel extends BaseViewModel {

    @Inject
    public SelectUserTypeViewModel(@NonNull Application application) {
        super(application);
    }

}
