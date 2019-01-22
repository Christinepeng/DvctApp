package com.divercity.android.features.groups.groupdetail.conversation.datasource
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.questions.QuestionResponse
import com.divercity.android.features.groups.groupdetail.conversation.usecase.FetchGroupConversationsCase
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class GroupConversationPaginatedRepositoryImpl @Inject
internal constructor(private val fetchGroupConversationsCase: FetchGroupConversationsCase){

    private lateinit var groupConversationDataSourceFactory: GroupConversationDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    companion object {

        const val pageSize = 10
    }

    fun fetchData(groupdId : String,query : String?): Listing<QuestionResponse> {

        val executor = Executors.newFixedThreadPool(5)

        fetchGroupConversationsCase.groupId = groupdId

        groupConversationDataSourceFactory =
                GroupConversationDataSourceFactory(
                    compositeDisposable,
                    fetchGroupConversationsCase,
                    query)

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build()

        val pagedList = LivePagedListBuilder(groupConversationDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build()

        return Listing(
                pagedList,
                Transformations.switchMap(groupConversationDataSourceFactory.groupsInterestsDataSource) { input -> input.networkState },
                Transformations.switchMap(groupConversationDataSourceFactory.groupsInterestsDataSource) { input -> input.initialLoad }
        )
    }

    fun retry() = groupConversationDataSourceFactory.groupsInterestsDataSource.value!!.retry()


    fun refresh() = groupConversationDataSourceFactory.groupsInterestsDataSource.value!!.invalidate()


    fun clear() = compositeDisposable.dispose()
}
