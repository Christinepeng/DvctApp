package com.divercity.android.features.company.companydetail

import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.divercity.android.R
import com.divercity.android.features.company.companydetail.about.CompanyDetailAboutFragment
import com.divercity.android.features.company.companydetail.employees.EmployeesFragment
import com.divercity.android.features.company.companydetail.jobpostings.JobPostingsByCompanyFragment
import com.divercity.android.features.company.diversityrating.DiversityRatingFragment
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class CompanyDetailViewPagerAdapter
@Inject constructor(
    val context: Context,
    fragment: CompanyDetailFragment
) : FragmentStatePagerAdapter(
    fragment.childFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    var registeredFragments = SparseArray<Fragment>()
    lateinit var companyId: String

    companion object {

        private const val PAGE_COUNT = 4
    }

    // Tab titles
    private val tabTitles: Array<String> = arrayOf(
        context.getString(R.string.about),
        context.getString(R.string.jobs),
        context.getString(R.string.employees),
        context.getString(R.string.diversity_rating)
    )

    override fun getItem(position: Int): Fragment {
        return getFragments(position)
    }

    private fun getFragments(position: Int): Fragment {
        return when (position) {
            0 -> CompanyDetailAboutFragment.newInstance()
            1 -> JobPostingsByCompanyFragment.newInstance(companyId)
            2 -> EmployeesFragment.newInstance(companyId)
            else -> DiversityRatingFragment.newInstance()
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
