package com.divercity.android.data.entity.recommendedjobsgoi

import com.divercity.android.data.entity.job.response.JobResponse
import com.google.gson.annotations.SerializedName

data class Jobs(

	@field:SerializedName("data")
	val data: List<JobResponse>? = null
)