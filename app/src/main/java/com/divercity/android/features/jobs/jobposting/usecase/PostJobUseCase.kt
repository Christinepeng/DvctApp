package com.divercity.android.features.jobs.jobposting.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.job.jobpostingbody.Job
import com.divercity.android.data.entity.job.jobpostingbody.JobBody
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class PostJobUseCase @Inject
constructor(
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler,
        private val repository: JobRepository
) : UseCase<JobResponse, PostJobUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<JobResponse> {
        val job = Job(
                title = params.title,
                description = params.desc,
                jobEmployerId = params.companyId,
                jobTypeId = params.typeId,
                locationDisplayName = params.location,
                skills = params.skills
        )
        return repository.postJob(JobBody(job))
    }

    class Params private constructor(
            val title: String,
            val desc: String,
            val companyId: String,
            val typeId: String,
            val location: String,
            val skills: List<String?>
    ) {

        companion object {

            fun forJob(
                    title: String,
                    desc: String,
                    companyId: String,
                    typeId: String,
                    location: String,
                    skills: List<String?>
            ): Params {
                return Params(title, desc, companyId, typeId, location, skills)
            }
        }
    }
}
