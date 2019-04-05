package com.divercity.android.features.company.companydetail.jobpostings.datasource

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.company.companydetail.jobpostings.usecase.FetchJobPostingsByCompanyUseCase
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class JobPostingsByCompanyRepositoryImpl @Inject
internal constructor(private val fetchJobPostingsByCompanyUseCase: FetchJobPostingsByCompanyUseCase) {

    private var jobDataSourceFactory: JobPostingsByCompanySourceFactory? = null

    companion object {

        const val pageSize = 20
    }

    fun fetchData(companyId: String): Listing<JobResponse> {

        val executor = Executors.newFixedThreadPool(5)

        fetchJobPostingsByCompanyUseCase.companyId = companyId
        jobDataSourceFactory = JobPostingsByCompanySourceFactory(fetchJobPostingsByCompanyUseCase)

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(false)
            .build()

        val pagedList = LivePagedListBuilder(jobDataSourceFactory!!, config)
            .setFetchExecutor(executor)
            .build()

        return Listing(
            pagedList,
            Transformations.switchMap(jobDataSourceFactory!!.jobPostingsByCompanyDataSource) { input -> input.networkState },
            Transformations.switchMap(jobDataSourceFactory!!.jobPostingsByCompanyDataSource) { input -> input.initialLoad }
        )
    }

    fun retry() {
        jobDataSourceFactory?.jobPostingsByCompanyDataSource?.value?.retry()
    }


    fun refresh() {
        jobDataSourceFactory?.jobPostingsByCompanyDataSource?.value?.invalidate()
    }

    fun clear() {
        jobDataSourceFactory?.jobPostingsByCompanyDataSource?.value?.dispose()
    }
}
