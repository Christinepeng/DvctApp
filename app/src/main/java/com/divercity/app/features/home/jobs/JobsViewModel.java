package com.divercity.app.features.home.jobs;

import android.app.Application;
import android.support.annotation.NonNull;

import com.divercity.app.core.base.BaseViewModel;

import javax.inject.Inject;

/**
 * Created by lucas on 16/10/2018.
 */

public class JobsViewModel extends BaseViewModel {

    @Inject
    public JobsViewModel(@NonNull Application application) {
        super(application);
    }

}
