package com.divercity.app.features.jobposting.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.job.jobpostingbody.Job
import com.divercity.app.data.entity.job.jobpostingbody.JobBody
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.repository.job.JobRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class EditJobUseCase @Inject
constructor(
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler,
        private val repository: JobRepository
) : UseCase<JobResponse, EditJobUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<JobResponse> {
        val job = Job(
                title = params.title,
                description = params.desc,
                jobEmployerId = params.companyId,
                jobTypeId = params.typeId,
                locationDisplayName = params.location,
                skills = params.skills
        )
        return repository.editJob(params.id, JobBody(job))
    }

    class Params private constructor(
            val id : String,
            val title: String,
            val desc: String,
            val companyId: String,
            val typeId: String,
            val location: String,
            val skills: List<String?>
    ) {

        companion object {

            fun forJob(
                    id : String,
                    title: String,
                    desc: String,
                    companyId: String,
                    typeId: String,
                    location: String,
                    skills: List<String?>
            ): Params {
                return Params(id, title, desc, companyId, typeId, location, skills)
            }
        }
    }
}
