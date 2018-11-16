package com.divercity.app.data.entity.job.jobpostingbody

import com.google.gson.annotations.SerializedName

data class Job(

	@field:SerializedName("lng")
	val lng: String? = null,

	@field:SerializedName("job_employer_id")
	val jobEmployerId: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("deadline")
	val deadline: String? = null,

	@field:SerializedName("job_type_id")
	val jobTypeId: String? = null,

	@field:SerializedName("lat")
	val lat: String? = null,

	@field:SerializedName("location_display_name")
	val locationDisplayName: String? = null,

	@field:SerializedName("skills_tag")
	val skills: List<String?>? = null
)