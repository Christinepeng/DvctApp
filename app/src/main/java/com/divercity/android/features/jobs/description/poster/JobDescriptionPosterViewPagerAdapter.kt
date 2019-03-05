package com.divercity.android.features.jobs.description.poster

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import com.divercity.android.R
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.jobs.description.aboutcompany.TabAboutCompanyFragment
import com.divercity.android.features.jobs.description.jobdescription.TabJobDescriptionFragment
import com.divercity.android.features.jobs.description.poster.similarjobs.SimilarJobsFragment
import com.divercity.android.repository.user.UserRepository
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class JobDescriptionPosterViewPagerAdapter
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

    fun getRegisteredFragment(position: Int): Fragment {
        return registeredFragments.get(position)
    }
}
