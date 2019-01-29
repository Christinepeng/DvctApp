package com.divercity.android.features.chat.recentchats.oldrecentchats.datasource

import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.divercity.android.core.base.PaginatedQueryRepository
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.features.chat.recentchats.usecase.FetchCurrentChatsUseCase
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class ChatsPaginatedRepositoryImpl @Inject
internal constructor(private val fetchCurrentChatsUseCase: FetchCurrentChatsUseCase) : PaginatedQueryRepository<ExistingUsersChatListItem> {

    private lateinit var chatsDataSourceFactory: ChatsDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    companion object {

        const val pageSize = 10
    }

    override fun fetchData(query : String?): Listing<ExistingUsersChatListItem> {

        val executor = Executors.newFixedThreadPool(5)

        chatsDataSourceFactory = ChatsDataSourceFactory(compositeDisposable, fetchCurrentChatsUseCase, query)

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build()

        val pagedList = LivePagedListBuilder(chatsDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build()

        return Listing(
                pagedList,
                Transformations.switchMap(chatsDataSourceFactory.groupsInterestsDataSource) { input -> input.networkState },
                Transformations.switchMap(chatsDataSourceFactory.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    override fun retry() = chatsDataSourceFactory.groupsInterestsDataSource.value!!.retry()


    override fun refresh() = chatsDataSourceFactory.groupsInterestsDataSource.value!!.invalidate()


    override fun clear() = compositeDisposable.dispose()
}
