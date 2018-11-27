package com.divercity.app.features.jobposting

import android.arch.lifecycle.MutableLiveData
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.company.CompanyResponse
import com.divercity.app.data.entity.job.jobtype.JobTypeResponse
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.data.entity.skills.SkillResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.jobposting.usecase.PostJobUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 05/11/2018.
 */

class JobPostingViewModel @Inject
constructor(private val postJobUseCase: PostJobUseCase) : BaseViewModel() {

    var postJobResponse = MutableLiveData<Resource<JobResponse>>()
    var company: CompanyResponse? = null
    var jobType: JobTypeResponse? = null
    var skillList = ArrayList<SkillResponse>()

    fun postJob(title: String,
                desc: String,
                companyId: String,
                typeId: String,
                location: String,
                skillList : List<String?>) {
        postJobResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobResponse>() {
            override fun onFail(error: String) {
                postJobResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                postJobResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobResponse) {
                postJobResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        postJobUseCase.execute(callback, PostJobUseCase.Params.forJob(title, desc, companyId, typeId, location, skillList))
    }

    fun getStringSkillList(): List<String?> {
        return skillList.map { it -> it.attributes?.name }
    }
}