package com.divercity.app.features.home.home.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.features.home.home.RecommendedItem
import com.divercity.app.repository.group.GroupRepository
import com.divercity.app.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchRecommendedJobsGroupsUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val jobRepository: JobRepository,
            private val groupRepository: GroupRepository
) : UseCase<@JvmSuppressWildcards List<RecommendedItem>, FetchRecommendedJobsGroupsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<RecommendedItem>> {

        val fetchJobs = jobRepository.fetchRecommendedJobs(
                params.page,
                params.size,
                params.query)

        val fetchGroups = groupRepository.fetchRecommendedGroups(
                params.page, params.size)

       return Observable.zip(
               fetchGroups,
               fetchJobs,
               BiFunction<List<GroupResponse>, List<JobResponse>, List<RecommendedItem>> {
                   t1, t2 ->
                   val res = ArrayList<RecommendedItem>()
                   res.addAll(t2)
                   res.addAll(t1)
                   res.shuffle()
                   return@BiFunction res
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
