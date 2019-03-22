package com.divercity.android.features.profile.module;

import com.divercity.android.features.profile.currentuser.CurrentUserProfileFragment;

import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 16/10/2018.
 */

@Module
public abstract class ProfileModule {

    @Provides
    static FragmentManager provideFragmentManager(CurrentUserProfileFragment currentUserProfileFragment) {
        return currentUserProfileFragment.getChildFragmentManager();
    }

//    @Provides
//    static FragmentManager provideFragmentManager(OtherUserProfileFragment otherUserProfileFragment) {
//        return otherUserProfileFragment.getChildFragmentManager();
//    }
}
