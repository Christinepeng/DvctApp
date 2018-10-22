package com.divercity.app.features.profile.profileprompt;

import android.app.Application;
import android.support.annotation.NonNull;

import com.divercity.app.core.base.BaseViewModel;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class ProfilePromptViewModel extends BaseViewModel {

    @Inject
    public ProfilePromptViewModel(@NonNull Application application) {
        super(application);
    }

}
