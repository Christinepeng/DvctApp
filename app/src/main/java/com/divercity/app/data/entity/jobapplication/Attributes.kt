package com.divercity.app.data.entity.jobapplication


import com.google.gson.annotations.SerializedName


data class Attributes(

	@field:SerializedName("cover_letter")
	val coverLetter: String? = null,

	@field:SerializedName("job_id")
	val jobId: Int? = null,

	@field:SerializedName("document")
	val document: String? = null,

	@field:SerializedName("employer")
	val employer: Employer? = null,

	@field:SerializedName("job_title")
	val jobTitle: String? = null,

	@field:SerializedName("user_document_id")
	val userDocumentId: Int? = null,

	@field:SerializedName("applicant")
	val applicant: Applicant? = null
)