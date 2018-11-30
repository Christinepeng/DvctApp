package com.divercity.app.features.jobs.description.detail

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import com.divercity.app.R
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.features.jobs.description.aboutcompany.TabAboutCompanyFragment
import com.divercity.app.features.jobs.description.jobdescription.TabJobDescriptionFragment
import com.divercity.app.features.jobs.description.detail.similarjobs.SimilarJobsFragment
import com.divercity.app.repository.user.UserRepository
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class JobDetailViewPagerAdapter
@Inject constructor(
        val context: Context,
        fm: FragmentManager,
        val userRepository: UserRepository
) : FragmentStatePagerAdapter(fm) {

    var registeredFragments = SparseArray<Fragment>()
    lateinit var job : JobResponse

    companion object {

        private const val PAGE_COUNT = 3
    }

    // Tab titles
    private val tabTitles: Array<String> = arrayOf(
            context.getString(R.string.description),
            context.getString(R.string.about_company),
            context.getString(R.string.similar_jobs)
    )

    override fun getItem(position: Int): Fragment {
        return getFragments(position)
    }

    private fun getFragments(position: Int): Fragment {
        return when (position) {
            0 -> TabJobDescriptionFragment.newInstance(job.attributes?.description)
            1 -> TabAboutCompanyFragment.newInstance(job.attributes?.employer)
            else -> SimilarJobsFragment.newInstance()
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
