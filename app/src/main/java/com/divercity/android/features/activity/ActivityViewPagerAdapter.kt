package com.divercity.android.features.activity

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import com.divercity.android.R
import com.divercity.android.features.activity.connectionrequests.ConnectionRequestsFragment
import com.divercity.android.features.activity.notifications.NotificationsFragment
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class ActivityViewPagerAdapter
@Inject constructor(
    val context: Context,
    fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

    private var registeredFragments = SparseArray<Fragment>()

    companion object {

        private const val PAGE_COUNT = 2
    }

    // Tab titles
    private val tabTitles: Array<String> = arrayOf(
        context.getString(R.string.notifications),
        context.getString(R.string.connection_requests)
    )

    override fun getItem(position: Int): Fragment {
        return getFragments(position)
    }

    private fun getFragments(position: Int): Fragment {
        return when (position) {
            0 -> NotificationsFragment.newInstance()
            else -> ConnectionRequestsFragment.newInstance()
        }
    }

    override fun getCount(): Int =
        PAGE_COUNT

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
