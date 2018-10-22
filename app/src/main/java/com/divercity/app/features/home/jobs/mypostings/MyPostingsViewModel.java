package com.divercity.app.features.home.jobs.mypostings;

import android.app.Application;
import android.support.annotation.NonNull;

import com.divercity.app.core.base.BaseViewModel;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class MyPostingsViewModel extends BaseViewModel {

    @Inject
    public MyPostingsViewModel(@NonNull Application application) {
        super(application);
    }

}
