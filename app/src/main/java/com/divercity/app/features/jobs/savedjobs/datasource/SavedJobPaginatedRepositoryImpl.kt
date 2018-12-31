package com.divercity.app.features.jobs.savedjobs.datasource

import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.divercity.app.core.base.PaginatedQueryRepository
import com.divercity.app.core.utils.Listing
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.features.jobs.savedjobs.usecase.FetchSavedJobsUseCase
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class SavedJobPaginatedRepositoryImpl @Inject
internal constructor(private val fetchJobsUseCase: FetchSavedJobsUseCase) : PaginatedQueryRepository<JobResponse> {

    private lateinit var jobDataSourceFactory: SavedJobDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    companion object {

        const val pageSize = 20
    }

    override fun fetchData(query : String?): Listing<JobResponse> {

        val executor = Executors.newFixedThreadPool(5)

        jobDataSourceFactory = SavedJobDataSourceFactory(compositeDisposable, fetchJobsUseCase, query)

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build()

        val pagedList = LivePagedListBuilder(jobDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build()

        return Listing(
                pagedList,
                Transformations.switchMap(jobDataSourceFactory.groupsInterestsDataSource) { input -> input.networkState },
                Transformations.switchMap(jobDataSourceFactory.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    override fun retry() = jobDataSourceFactory.groupsInterestsDataSource.value!!.retry()


    override fun refresh() = jobDataSourceFactory.groupsInterestsDataSource.value!!.invalidate()


    override fun clear() = compositeDisposable.dispose()
}
