package com.divercity.android.data.entity.education.body

import com.google.gson.annotations.SerializedName

data class EducationEntity(

    @field:SerializedName("qualification")
    val qualification: String? = null,

    @field:SerializedName("school_id")
    val schoolId: String? = null,

    @field:SerializedName("start_year")
    val startYear: String? = null,

    @field:SerializedName("end_year")
    val endYear: String? = null,

    @field:SerializedName("degree_id")
    val degreeId: String? = null
)