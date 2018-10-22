package com.divercity.app.features.home.jobs;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.divercity.app.R;
import com.divercity.app.features.home.jobs.jobs.JobsListFragment;
import com.divercity.app.features.home.jobs.applications.JobsApplicationsFragment;
import com.divercity.app.features.home.jobs.saved.JobsSavedFragment;

import javax.inject.Inject;

/**
 * Created by lucas on 16/10/2018.
 */

public class JobsViewPagerAdapter extends FragmentStatePagerAdapter {
    private final int PAGE_COUNT = 3;
    // Tab titles
    private String tabTitles[];

    @Inject
    public JobsViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        tabTitles = new String[]{
                context.getString(R.string.jobs),
                context.getString(R.string.applications),
                context.getString(R.string.saved)
        };
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = JobsListFragment.newInstance();
                break;
            case 1:
                fragment = JobsApplicationsFragment.newInstance();
                break;
            default:
                fragment = JobsSavedFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
