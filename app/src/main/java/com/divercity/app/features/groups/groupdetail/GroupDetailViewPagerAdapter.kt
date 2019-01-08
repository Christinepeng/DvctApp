package com.divercity.app.features.groups.groupdetail

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import com.divercity.app.R
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.features.groups.groupdetail.about.TabAboutGroupDetailFragment
import com.divercity.app.features.groups.groupdetail.conversation.GroupConversationFragment
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class GroupDetailViewPagerAdapter
@Inject constructor(
        val context: Context,
        fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

    private var registeredFragments = SparseArray<Fragment>()

    lateinit var group : GroupResponse

    companion object {

        private const val PAGE_COUNT = 2
    }

    // Tab titles
    private val tabTitles: Array<String> = arrayOf(
            context.getString(R.string.about),
            context.getString(R.string.conversation)
    )

    override fun getItem(position: Int): Fragment {
        return getFragments(position)
    }

    private fun getFragments(position: Int): Fragment {
        return when (position) {
            0 -> TabAboutGroupDetailFragment.newInstance(group)
            else -> GroupConversationFragment.newInstance(group.id)
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
