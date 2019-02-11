package com.divercity.android.features.home.home.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.home.HomeItem
import com.divercity.android.data.entity.home.Recommended
import com.divercity.android.data.entity.home.RecommendedItem
import com.divercity.android.repository.feed.FeedRepository
import com.divercity.android.repository.group.GroupRepository
import com.divercity.android.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.Function3
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchFeedRecommendedJobsGroupsUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val jobRepository: JobRepository,
    private val groupRepository: GroupRepository,
    private val feedRepository: FeedRepository
) : UseCase<@JvmSuppressWildcards List<HomeItem>, FetchFeedRecommendedJobsGroupsUseCase.Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<List<HomeItem>> {

        val fetchJobs = jobRepository.fetchRecommendedJobs(
            0,
            5,
            null
        )

        val fetchGroups = groupRepository.fetchRecommendedGroups(
            0,
            5
        )

        val fetchQuestions = feedRepository.fetchQuestions(
            params.page,
            params.size
        )

        return Observable.zip(
            fetchGroups,
            fetchJobs,
            fetchQuestions,
            Function3 { t1, t2, t3 ->
                val recommendedRes = ArrayList<RecommendedItem>()
                recommendedRes.addAll(t2)
                recommendedRes.addAll(t1)
                recommendedRes.shuffle()

                val recommended = Recommended(recommendedRes)

                val res = ArrayList<HomeItem>()
                res.add(recommended)
                res.addAll(t3)

                return@Function3 res
            }
        )
    }

    class Params private constructor(val page: Int, val size: Int, val query: String?) {

        companion object {

            fun forJobs(page: Int, size: Int, query: String?): Params {
                return Params(page, size, query)
            }
        }
    }
}
