package com.divercity.android.features.groups

import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.divercity.android.R
import com.divercity.android.features.groups.allgroups.AllGroupsFragment
import com.divercity.android.features.groups.yourgroups.YourGroupsFragment
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabGroupsViewPagerAdapter
@Inject constructor(
        val context: Context,
        fragment: TabGroupsFragment
) : FragmentStatePagerAdapter(fragment.childFragmentManager) {

    private var registeredFragments = SparseArray<Fragment>()

    companion object {

        private const val PAGE_COUNT = 2
    }

    // Tab titles
    private val tabTitles: Array<String> = arrayOf(
            context.getString(R.string.your_groups),
            context.getString(R.string.discover)
    )

    override fun getItem(position: Int): Fragment {
        return getFragments(position)
    }

    private fun getFragments(position: Int): Fragment {
        return when (position) {
            0 -> YourGroupsFragment.newInstance()
            else -> AllGroupsFragment.newInstance()
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

    fun getRegisteredFragment(position: Int): Fragment? {
        return registeredFragments.get(position)
    }
}
