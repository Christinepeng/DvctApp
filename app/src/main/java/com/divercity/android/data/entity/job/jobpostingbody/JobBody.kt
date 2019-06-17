package com.divercity.android.data.entity.job.jobpostingbody

import com.google.gson.annotations.SerializedName

data class JobBody(

	@field:SerializedName("companyName")
	val job: Job? = null
)