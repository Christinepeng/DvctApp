package com.divercity.android.features.home.people

import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.divercity.android.R
import com.divercity.android.features.company.companies.CompaniesFragment
import com.divercity.android.features.groups.allgroups.AllGroupsFragment
import com.divercity.android.features.user.allconnections.AllConnectionsFragment
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabPeopleViewPagerAdapter
@Inject constructor(
    val context: Context,
    fragment: TabPeopleFragment
) : FragmentStatePagerAdapter(
    fragment.childFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private var registeredFragments = SparseArray<Fragment>()

    companion object {

        private const val PAGE_COUNT = 3
    }

    // Tab titles
    private val tabTitles: Array<String> = arrayOf(
        context.getString(R.string.connections),
        context.getString(R.string.companies),
        context.getString(R.string.groups)
    )

    override fun getItem(position: Int): Fragment {
        return getFragments(position)
    }

    private fun getFragments(position: Int): Fragment {
        return when (position) {
            0 -> AllConnectionsFragment.newInstance()
            1 -> CompaniesFragment.newInstance()
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
