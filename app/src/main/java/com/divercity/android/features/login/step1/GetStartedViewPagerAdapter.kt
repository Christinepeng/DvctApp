package com.divercity.android.features.login.step1

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class GetStartedViewPagerAdapter
@Inject constructor(
    val context: Context,
    fragment: GetStartedFragment
) : FragmentPagerAdapter(
    fragment.childFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    val fragments = listOf(
        GetStartedJoinCommunitiesFragment(),
        GetStartedFindJobsFragment(),
        GetStartedRecruitFragment()
    )

    override fun getItem(position: Int): Fragment {
        return getFragments(position)
    }

    private fun getFragments(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int = fragments.size
}
