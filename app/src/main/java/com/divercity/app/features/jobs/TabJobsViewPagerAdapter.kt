package com.divercity.app.features.jobs

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup

import com.divercity.app.R
import com.divercity.app.features.jobs.applications.JobsApplicationsFragment
import com.divercity.app.features.jobs.jobs.JobsListFragment
import com.divercity.app.features.jobs.mypostings.MyJobsPostingsFragment
import com.divercity.app.features.jobs.saved.SavedJobsFragment
import com.divercity.app.repository.user.UserRepository

import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabJobsViewPagerAdapter
@Inject constructor(
        val context: Context,
        fm: FragmentManager,
        val userRepository: UserRepository
) : FragmentStatePagerAdapter(fm) {

    private val PAGE_COUNT = 3

    private var registeredFragments = SparseArray<Fragment>()

    // Tab titles
    private val tabTitlesRecruiter: Array<String> = arrayOf(
            context.getString(R.string.my_postings),
            context.getString(R.string.jobs),
            context.getString(R.string.saved)
    )


    // Tab titles
    private val tabTitlesJobSeeker: Array<String> = arrayOf(
            context.getString(R.string.jobs),
            context.getString(R.string.applications),
            context.getString(R.string.saved)
    )

    override fun getItem(position: Int): Fragment {
        return if (userRepository.getAccountType() != null &&
                (userRepository.getAccountType().equals(context.getString(R.string.hiring_manager_id)) ||
                        userRepository.getAccountType().equals(context.getString(R.string.recruiter_id)))
        )
            getFragmentsRecruiter(position)
        else
            getFragmentsSeeker(position)
    }

    private fun getFragmentsSeeker(position: Int): Fragment {
        return when (position) {
            0 -> JobsListFragment.newInstance()
            1 -> JobsApplicationsFragment.newInstance()
            else -> SavedJobsFragment.newInstance()
        }
    }

    private fun getFragmentsRecruiter(position: Int): Fragment {
        return when (position) {
            0 -> MyJobsPostingsFragment.newInstance()
            1 -> JobsListFragment.newInstance()
            else -> SavedJobsFragment.newInstance()
        }
    }

    override fun getCount(): Int = PAGE_COUNT


    override fun getPageTitle(position: Int): CharSequence? {
        return if (userRepository.getAccountType() != null &&
                (userRepository.getAccountType().equals(context.getString(R.string.job_seeker_id)) ||
                        userRepository.getAccountType().equals(context.getString(R.string.student_id))) ||
                userRepository.getAccountType().equals(context.getString(R.string.professional_id))
        )
            tabTitlesJobSeeker[position]
        else
            tabTitlesRecruiter[position]
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
