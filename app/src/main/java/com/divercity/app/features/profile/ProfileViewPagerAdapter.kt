package com.divercity.app.features.profile

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import com.divercity.app.R
import com.divercity.app.features.groups.trending.TrendingGroupsFragment
import com.divercity.app.features.profile.tabfollowers.FollowerFragment
import com.divercity.app.features.profile.tabprofile.TabProfileFragment
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class ProfileViewPagerAdapter
@Inject constructor(
        val context: Context,
        fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

    private var registeredFragments = SparseArray<Fragment>()

    companion object {

        private const val PAGE_COUNT = 4
    }

    // Tab titles
    private val tabTitles: Array<String> = arrayOf(
            context.getString(R.string.profile),
            context.getString(R.string.followers),
            context.getString(R.string.following),
            context.getString(R.string.groups)
    )

    override fun getItem(position: Int): Fragment {
        return getFragments(position)
    }

    private fun getFragments(position: Int): Fragment {
        return when (position) {
            0 -> TabProfileFragment.newInstance()
            1 -> FollowerFragment.newInstance()
            2 -> TabProfileFragment.newInstance()
            else -> TabProfileFragment.newInstance()
        }
    }

    override fun getCount(): Int = PAGE_COUNT

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(position: Int): Fragment {
        return registeredFragments.get(position)
    }
}
