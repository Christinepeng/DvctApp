package com.divercity.android.features.company.companydetail.jobpostings.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.company.companydetail.jobpostings.usecase.FetchJobPostingsByCompanyUseCase

class JobPostingsByCompanySourceFactory(
    private val fetchJobPostingsByCompanyUseCase: FetchJobPostingsByCompanyUseCase
) : DataSource.Factory<Long, JobResponse>() {

    val jobPostingsByCompanyDataSource = MutableLiveData<JobPostingsByCompanyDataSource>()

    override fun create(): DataSource<Long, JobResponse> {
        val data = JobPostingsByCompanyDataSource(fetchJobPostingsByCompanyUseCase)
        jobPostingsByCompanyDataSource.postValue(data)
        return data
    }
}
