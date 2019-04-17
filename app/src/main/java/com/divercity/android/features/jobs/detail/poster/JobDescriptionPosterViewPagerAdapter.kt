package com.divercity.android.features.jobs.detail.poster

import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.divercity.android.R
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.jobs.detail.aboutcompany.TabAboutCompanyFragment
import com.divercity.android.features.jobs.detail.jobdescription.TabJobDescriptionFragment
import com.divercity.android.features.jobs.detail.poster.similarjobs.SimilarJobsFragment
import com.divercity.android.repository.user.UserRepository
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class JobDescriptionPosterViewPagerAdapter
@Inject constructor(
    val context: Context,
    fragment: JobDescriptionPosterFragment,
    val userRepository: UserRepository
) : FragmentStatePagerAdapter(fragment.childFragmentManager) {

    var registeredFragments = SparseArray<Fragment>()
    lateinit var job : JobResponse

    companion object {

        private const val PAGE_COUNT = 3
    }

    // Tab titles
    private val tabTitles: Array<String> = arrayOf(
            context.getString(R.string.description),
            context.getString(R.string.about_company),
            context.getString(R.string.recommended_applicants)
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

    fun getRegisteredFragment(position: Int): Fragment? {
        return registeredFragments.get(position)
    }
}
