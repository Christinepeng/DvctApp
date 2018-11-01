package com.divercity.app.data.entity.job

import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("is_bookmarked_by_current")
	val isBookmarkedByCurrent: Boolean? = null,

	@field:SerializedName("images")
	val images: Images? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("job_type_info")
	val jobTypeInfo: JobTypeInfo? = null,

	@field:SerializedName("recruiter")
	val recruiter: Recruiter? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("employer")
	val employer: Employer? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("deadline")
	val deadline: String? = null
)