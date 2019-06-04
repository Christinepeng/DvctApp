package com.divercity.android.features.jobs

import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.divercity.android.R
import com.divercity.android.features.company.companiesmycompanies.CompaniesMyCompaniesFragment
import com.divercity.android.features.jobs.applications.JobsApplicationsFragment
import com.divercity.android.features.jobs.jobs.JobsListFragment
import com.divercity.android.features.jobs.mypostings.MyJobsPostingsFragment
import com.divercity.android.features.jobs.savedjobs.SavedJobsFragment
import com.divercity.android.repository.session.SessionRepository
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabJobsViewPagerAdapter
@Inject constructor(
    val context: Context,
    fm: TabJobsFragment,
    private val sessionRepository: SessionRepository
) : FragmentStatePagerAdapter(fm.childFragmentManager) {

    companion object {
        private const val PAGE_COUNT = 4
    }

    private var registeredFragments = SparseArray<Fragment>()

    // Tab titles
    private val tabTitlesRecruiter: Array<String> = arrayOf(
        context.getString(R.string.my_postings),
        context.getString(R.string.jobs),
        context.getString(R.string.companies),
        context.getString(R.string.saved)

    )


    // Tab titles
    private val tabTitlesJobSeeker: Array<String> = arrayOf(
        context.getString(R.string.jobs),
        context.getString(R.string.applications),
        context.getString(R.string.companies),
        context.getString(R.string.saved)
    )

    override fun getItem(position: Int): Fragment {
        return if (sessionRepository.isLoggedUserJobSeeker())
            getFragmentsSeeker(position)
        else
            getFragmentsRecruiter(position)
    }

    private fun getFragmentsSeeker(position: Int): Fragment {
        return when (position) {
            0 -> JobsListFragment.newInstance()
            1 -> JobsApplicationsFragment.newInstance()
            2 -> CompaniesMyCompaniesFragment.newInstance()
            else -> SavedJobsFragment.newInstance()
        }
    }

    private fun getFragmentsRecruiter(position: Int): Fragment {
        return when (position) {
            0 -> MyJobsPostingsFragment.newInstance()
            1 -> JobsListFragment.newInstance()
            2 -> CompaniesMyCompaniesFragment.newInstance()
            else -> SavedJobsFragment.newInstance()
        }
    }

    override fun getCount(): Int = PAGE_COUNT


    override fun getPageTitle(position: Int): CharSequence? {
        return if (sessionRepository.isLoggedUserJobSeeker())
            tabTitlesJobSeeker[position]
        else
            tabTitlesRecruiter[position]
    }

    fun getJobsTabPosition(): Int {
        return if (sessionRepository.isLoggedUserJobSeeker()) 0 else 1
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
