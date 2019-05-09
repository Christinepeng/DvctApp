package com.divercity.android.data.entity.job.discardjob

import com.google.gson.annotations.SerializedName

data class DiscardJobsEntityBody(

	@field:SerializedName("jobs")
	val jobs: List<String?>? = null
)