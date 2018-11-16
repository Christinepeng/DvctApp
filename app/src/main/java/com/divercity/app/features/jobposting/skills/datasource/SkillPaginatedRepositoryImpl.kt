package com.divercity.app.features.jobposting.skills.datasource

import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.divercity.app.core.base.PaginatedQueryRepository
import com.divercity.app.core.utils.Listing
import com.divercity.app.data.entity.skills.SkillResponse
import com.divercity.app.features.jobposting.skills.usecase.FetchSkillsUseCase
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class SkillPaginatedRepositoryImpl @Inject
internal constructor(private val fetchSkillsUseCase: FetchSkillsUseCase) : PaginatedQueryRepository<SkillResponse> {

    private lateinit var skillDataSourceFactory: SkillDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    companion object {

        const val pageSize = 15
    }

    override fun fetchData(query : String?): Listing<SkillResponse> {

        val executor = Executors.newFixedThreadPool(5)

        skillDataSourceFactory = SkillDataSourceFactory(compositeDisposable, fetchSkillsUseCase, query)

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build()

        val pagedList = LivePagedListBuilder(skillDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build()

        return Listing(
                pagedList,
                Transformations.switchMap(skillDataSourceFactory.groupsInterestsDataSource) { input -> input.networkState },
                Transformations.switchMap(skillDataSourceFactory.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    override fun retry() = skillDataSourceFactory.groupsInterestsDataSource.value!!.retry()


    override fun refresh() = skillDataSourceFactory.groupsInterestsDataSource.value!!.invalidate()


    override fun clear() = compositeDisposable.dispose()
}
