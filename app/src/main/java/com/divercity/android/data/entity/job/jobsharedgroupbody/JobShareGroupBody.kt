package com.divercity.android.data.entity.job.jobsharedgroupbody

import com.google.gson.annotations.SerializedName

data class JobShareGroupBody(

	@field:SerializedName("group_of_interests")
	val groupOfInterests: List<String?>? = null
)