package com.divercity.android.data.entity.workexperience.response

import com.divercity.android.model.WorkExperience
import com.google.gson.annotations.SerializedName

data class WorkExperienceEntityResponse(

    @field:SerializedName("attributes")
    val attributes: Attributes? = null,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("type")
    val type: String? = null
) {
    fun toWorkExperience() = WorkExperience(
        id,
        attributes?.jobEmployerInfo?.name,
        attributes?.jobEmployerInfo?.pictureMain,
        attributes?.role,
        attributes?.jobStart,
        attributes?.jobEnd,
        attributes?.isPresent,
        attributes?.experienceDetails
    )
}