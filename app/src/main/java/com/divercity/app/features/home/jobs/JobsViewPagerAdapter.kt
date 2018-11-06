package com.divercity.app.features.home.jobs

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import com.divercity.app.R
import com.divercity.app.features.home.jobs.applications.JobsApplicationsFragment
import com.divercity.app.features.home.jobs.jobs.JobsListFragment
import com.divercity.app.features.home.jobs.mypostings.MyPostingsFragment
import com.divercity.app.features.home.jobs.saved.SavedJobsFragment
import com.divercity.app.repository.user.UserRepository

import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class JobsViewPagerAdapter
@Inject constructor(
        val context: Context,
        fm: FragmentManager,
        val userRepository: UserRepository
) : FragmentStatePagerAdapter(fm) {

    private val PAGE_COUNT = 3

    // Tab titles
    private val tabTitlesRecruiter: Array<String> = arrayOf(
            context.getString(R.string.applications),
            context.getString(R.string.my_postings),
            context.getString(R.string.jobs)
    )


    // Tab titles
    private val tabTitlesJobSeeker: Array<String> = arrayOf(
            context.getString(R.string.jobs),
            context.getString(R.string.applications),
            context.getString(R.string.saved)
    )

    override fun getItem(position: Int): Fragment {
        return if (userRepository.getAccountType() != null &&
                (userRepository.getAccountType().equals(context.getString(R.string.job_seeker_id)) ||
                        userRepository.getAccountType().equals(context.getString(R.string.student_id)))
        )
            getFragmentsSeeker(position)
        else
            getFragmentsRecruiter(position)
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
            0 -> JobsApplicationsFragment.newInstance()
            1 -> MyPostingsFragment.newInstance()
            else -> JobsListFragment.newInstance()
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
}
