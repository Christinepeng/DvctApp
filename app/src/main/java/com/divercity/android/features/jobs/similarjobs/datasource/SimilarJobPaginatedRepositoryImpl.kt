package com.divercity.android.features.jobs.similarjobs.datasource

import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.jobs.similarjobs.usecase.FetchSimilarJobsUseCase
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class SimilarJobPaginatedRepositoryImpl @Inject
internal constructor(private val fetchSimilarJobsUseCase: FetchSimilarJobsUseCase) {

    private lateinit var similarJobDataSourceFactory: SimilarJobDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    companion object {

        const val pageSize = 20
    }

    fun fetchData(jobId : String): Listing<JobResponse> {

        val executor = Executors.newFixedThreadPool(5)

        fetchSimilarJobsUseCase.jobId = jobId

        similarJobDataSourceFactory = SimilarJobDataSourceFactory(compositeDisposable, fetchSimilarJobsUseCase)

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build()

        val pagedList = LivePagedListBuilder(similarJobDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build()

        return Listing(
                pagedList,
                Transformations.switchMap(similarJobDataSourceFactory.groupsInterestsDataSource) { input -> input.networkState },
                Transformations.switchMap(similarJobDataSourceFactory.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    fun retry() = similarJobDataSourceFactory.groupsInterestsDataSource.value!!.retry()


    fun refresh() = similarJobDataSourceFactory.groupsInterestsDataSource.value!!.invalidate()


    fun clear() = compositeDisposable.dispose()
}
