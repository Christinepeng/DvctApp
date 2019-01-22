package com.divercity.android.features.jobs.applicants.datasource

import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.jobapplication.JobApplicationResponse
import com.divercity.android.features.jobs.applicants.usecase.FetchJobsApplicantsUseCase
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class JobApplicantsPaginatedRepositoryImpl @Inject
internal constructor(private val fetchJobsApplicantsUseCase: FetchJobsApplicantsUseCase) {

    private lateinit var jobApplicantsDataSourceFactory: JobApplicantsDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    companion object {

        const val pageSize = 20
    }

    fun fetchData(jobId : String): Listing<JobApplicationResponse> {

        val executor = Executors.newFixedThreadPool(5)

        fetchJobsApplicantsUseCase.jobId = jobId
        jobApplicantsDataSourceFactory = JobApplicantsDataSourceFactory(compositeDisposable, fetchJobsApplicantsUseCase)

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build()

        val pagedList = LivePagedListBuilder(jobApplicantsDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build()

        return Listing(
                pagedList,
                Transformations.switchMap(jobApplicantsDataSourceFactory.groupsInterestsDataSource) { input -> input.networkState },
                Transformations.switchMap(jobApplicantsDataSourceFactory.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    fun retry() = jobApplicantsDataSourceFactory.groupsInterestsDataSource.value!!.retry()


    fun refresh() = jobApplicantsDataSourceFactory.groupsInterestsDataSource.value!!.invalidate()


    fun clear() = compositeDisposable.dispose()
}
