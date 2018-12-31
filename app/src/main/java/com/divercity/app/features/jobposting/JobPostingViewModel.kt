package com.divercity.app.features.jobposting

import android.arch.lifecycle.MutableLiveData
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.company.response.CompanyResponse
import com.divercity.app.data.entity.job.jobtype.JobTypeResponse
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.data.entity.skills.Attributes
import com.divercity.app.data.entity.skills.SkillResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.jobposting.usecase.EditJobUseCase
import com.divercity.app.features.jobposting.usecase.PostJobUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 05/11/2018.
 */

class JobPostingViewModel @Inject
constructor(private val postJobUseCase: PostJobUseCase,
            private val editJobUseCase: EditJobUseCase) : BaseViewModel() {

    var jobResponse = MutableLiveData<Resource<JobResponse>>()
    var company: CompanyResponse? = null
    var jobType: JobTypeResponse? = null
    var skillList = ArrayList<SkillResponse>()

    fun postJob(title: String,
                desc: String,
                location: String) {
        jobResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobResponse>() {
            override fun onFail(error: String) {
                jobResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                jobResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobResponse) {
                jobResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        postJobUseCase.execute(callback, PostJobUseCase.Params.forJob
            (title, desc, company!!.id!!, jobType!!.id!!, location, getStringSkillList()))
    }

    fun editJob(id: String,
                title: String,
                desc: String,
                location: String) {
        jobResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobResponse>() {
            override fun onFail(error: String) {
                jobResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                jobResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobResponse) {
                jobResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        editJobUseCase.execute(callback, EditJobUseCase.Params.forJob
            (id, title, desc, company!!.id!!, jobType!!.id!!, location, getStringSkillList()))
    }

    private fun getStringSkillList(): List<String?> {
        return skillList.map { it -> it.attributes?.name }
    }

    fun setJobData(jobResponse: JobResponse){
        company = CompanyResponse(jobResponse.id)
        jobType = JobTypeResponse(id = "1")
        jobResponse.attributes?.skillsTag?.let {

            for(item in it){
                skillList.add(SkillResponse(attributes = Attributes(item)))
            }
        }
    }
}