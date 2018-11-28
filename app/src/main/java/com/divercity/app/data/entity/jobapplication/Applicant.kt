package com.divercity.app.data.entity.jobapplication

import com.google.gson.annotations.SerializedName

data class Applicant(

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("photos")
        val photos: Photos? = null
)