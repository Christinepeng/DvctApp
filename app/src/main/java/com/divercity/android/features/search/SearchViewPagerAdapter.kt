package com.divercity.android.features.search

import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.divercity.android.R
import com.divercity.android.features.company.companies.CompaniesFragment
import com.divercity.android.features.groups.allgroups.AllGroupsFragment
import com.divercity.android.features.jobs.jobs.JobsListFragment
import com.divercity.android.features.user.allconnections.AllConnectionsFragment
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class SearchViewPagerAdapter
@Inject constructor(
    val context: Context,
    fm: SearchFragment
) : FragmentStatePagerAdapter(fm.childFragmentManager) {

    companion object {
        private const val PAGE_COUNT = 4
    }

    private var registeredFragments = SparseArray<Fragment>()

    // Tab titles
    private val tabTitles: Array<String> = arrayOf(
        context.getString(R.string.groups),
        context.getString(R.string.people),
        context.getString(R.string.jobs),
        context.getString(R.string.companies)
    )


    override fun getItem(position: Int): Fragment {
        return getFragment(position)
    }

    private fun getFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllGroupsFragment.newInstance()
            1 -> AllConnectionsFragment.newInstance()
            2 -> JobsListFragment.newInstance()
            else -> CompaniesFragment.newInstance()
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
