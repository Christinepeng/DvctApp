package com.divercity.android.data.entity.recommendedjobsgoi

import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.job.response.JobResponse
import com.google.gson.annotations.SerializedName

data class RecommendedJobsGOIResponse(

	@field:SerializedName("goi")
	val goi: List<GroupResponse>? = null,

	@field:SerializedName("jobs")
	val jobs: List<JobResponse>? = null
)