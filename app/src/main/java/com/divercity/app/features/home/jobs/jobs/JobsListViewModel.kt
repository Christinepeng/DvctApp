package com.divercity.app.features.home.jobs.jobs

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Listing
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.job.JobResponse
import com.divercity.app.data.entity.login.response.LoginResponse
import com.divercity.app.data.entity.school.SchoolResponse
import com.divercity.app.features.home.jobs.jobs.job.JobPaginatedRepositoryImpl
import com.divercity.app.features.onboarding.usecase.UpdateUserProfileUseCase
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class JobsListViewModel @Inject
constructor(private val repository: JobPaginatedRepositoryImpl,
            private val updateUserProfileUseCase: UpdateUserProfileUseCase) : BaseViewModel() {

    var pagedJobsList: LiveData<PagedList<JobResponse>>? = null

    var listingPaginatedJob: Listing<JobResponse>? = null

    val updateUserProfileResponse = MutableLiveData<Resource<LoginResponse>>()

    init {
        fetchJobs()
    }

    fun networkState(): LiveData<NetworkState> = listingPaginatedJob!!.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedJob!!.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchJobs() {
        listingPaginatedJob = repository.fetchData()
        pagedJobsList = listingPaginatedJob?.pagedList
    }

    fun updateUserProfile(schoolResponse: SchoolResponse) {
//        updateUserProfileResponse.postValue(Resource.loading(null))
//        val callback = object : DisposableObserverWrapper<LoginResponse>() {
//            override fun onFail(error: String) {
//                updateUserProfileResponse.postValue(Resource.error(error, null))
//            }
//
//            override fun onHttpException(error: JsonElement) {
//                updateUserProfileResponse.postValue(Resource.error(error.toString(), null))
//            }
//
//            override fun onSuccess(o: LoginResponse) {
//                updateUserProfileResponse.postValue(Resource.success(o))
//            }
//        }
//        compositeDisposable.add(callback)
//        val user = User()
//        user.schoolId = Integer.parseInt(schoolResponse.id)
//        updateUserProfileUseCase.execute(callback, UpdateUserProfileUseCase.Params.forUser(user))
    }
}
