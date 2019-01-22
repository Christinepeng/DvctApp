package com.divercity.android.features.groups

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import com.divercity.android.R
import com.divercity.android.features.groups.all.AllGroupsFragment
import com.divercity.android.features.groups.mygroups.MyGroupsFragment
import com.divercity.android.features.groups.trending.TrendingGroupsFragment
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabGroupsViewPagerAdapter
@Inject constructor(
        val context: Context,
        fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

    private var registeredFragments = SparseArray<Fragment>()

    companion object {

        private const val PAGE_COUNT = 3
    }

    // Tab titles
    private val tabTitles: Array<String> = arrayOf(
            context.getString(R.string.all),
            context.getString(R.string.trending),
            context.getString(R.string.my_groups)
    )

    override fun getItem(position: Int): Fragment {
        return getFragments(position)
    }

    private fun getFragments(position: Int): Fragment {
        return when (position) {
            0 -> AllGroupsFragment.newInstance()
            1 -> TrendingGroupsFragment.newInstance()
            else -> MyGroupsFragment.newInstance()
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
