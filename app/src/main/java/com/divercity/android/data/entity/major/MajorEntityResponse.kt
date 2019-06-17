package com.divercity.android.data.entity.major

import com.divercity.android.model.Major
import com.google.gson.annotations.SerializedName

data class MajorEntityResponse(

    @field:SerializedName("attributes")
    val attributes: Attributes? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null
) {
    fun toMajor() = Major(id!!, attributes?.name)
}