package com.divercity.android.features.user.profileotheruser

import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.divercity.android.R
import com.divercity.android.features.user.userconnections.ConnectionsFragment
import com.divercity.android.features.user.profileotheruser.tabprofile.TabOtherUserProfileFragment
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class OtherUserProfileViewPagerAdapter
@Inject constructor(
    val context: Context,
    fragment: OtherUserProfileFragment
) : FragmentStatePagerAdapter(fragment.childFragmentManager) {

    private var registeredFragments = SparseArray<Fragment>()
    lateinit var userId: String

    companion object {

        private const val PAGE_COUNT = 2
    }

    // Tab titles
    private val tabTitles: Array<String> = arrayOf(
        context.getString(R.string.profile),
        context.getString(R.string.connections)
    )

    override fun getItem(position: Int): Fragment {
        return getFragments(position)
    }

    private fun getFragments(position: Int): Fragment {
        return when (position) {
            0 -> TabOtherUserProfileFragment.newInstance()
            else -> ConnectionsFragment.newInstance(userId)
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

    fun getRegisteredFragment(position: Int): Fragment? {
        return registeredFragments.get(position)
    }
}
