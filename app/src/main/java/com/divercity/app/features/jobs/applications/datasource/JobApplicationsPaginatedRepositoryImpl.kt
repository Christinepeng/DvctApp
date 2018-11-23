package com.divercity.app.features.jobs.applications.datasource

import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.divercity.app.core.base.PaginatedQueryRepository
import com.divercity.app.core.utils.Listing
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.features.jobs.applications.usecase.FetchJobsApplicationsUseCase
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class JobApplicationsPaginatedRepositoryImpl @Inject
internal constructor(private val fetchJobsApplicationsUseCase: FetchJobsApplicationsUseCase) : PaginatedQueryRepository<JobResponse> {

    private lateinit var jobApplicationsDataSourceFactory: JobApplicationsDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    companion object {

        const val pageSize = 20
    }

    override fun fetchData(query : String?): Listing<JobResponse> {

        val executor = Executors.newFixedThreadPool(5)

        jobApplicationsDataSourceFactory = JobApplicationsDataSourceFactory(compositeDisposable, fetchJobsApplicationsUseCase, query)

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build()

        val pagedList = LivePagedListBuilder(jobApplicationsDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build()

        return Listing(
                pagedList,
                Transformations.switchMap(jobApplicationsDataSourceFactory.groupsInterestsDataSource) { input -> input.networkState },
                Transformations.switchMap(jobApplicationsDataSourceFactory.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    override fun retry() = jobApplicationsDataSourceFactory.groupsInterestsDataSource.value!!.retry()


    override fun refresh() = jobApplicationsDataSourceFactory.groupsInterestsDataSource.value!!.invalidate()


    override fun clear() = compositeDisposable.dispose()
}
