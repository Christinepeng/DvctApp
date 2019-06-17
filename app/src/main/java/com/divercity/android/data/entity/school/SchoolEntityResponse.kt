package com.divercity.android.data.entity.school

import com.divercity.android.model.School
import com.google.gson.annotations.SerializedName

data class SchoolEntityResponse(

    @field:SerializedName("attributes")
    val attributes: Attributes? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null
) {

    fun toSchool() = School(id!!, attributes?.name, attributes?.logo)
}