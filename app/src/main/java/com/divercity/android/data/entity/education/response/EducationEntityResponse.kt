package com.divercity.android.data.entity.education.response

import com.google.gson.annotations.SerializedName

data class EducationEntityResponse(

    @field:SerializedName("attributes")
    val attributes: Attributes? = null,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("type")
    val type: String? = null
)