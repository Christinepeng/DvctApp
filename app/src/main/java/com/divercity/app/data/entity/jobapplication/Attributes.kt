package com.divercity.app.data.entity.jobapplication

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Attributes(

	@field:SerializedName("canceled")
	val canceled: Boolean? = null,

	@field:SerializedName("document_name")
	val documentName: String? = null,

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

	@field:SerializedName("document_last_used")
	val documentLastUsed: String? = null,

	@field:SerializedName("user_document_id")
	val userDocumentId: Int? = null,

	@field:SerializedName("applicant")
	val applicant: Applicant? = null
)