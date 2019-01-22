package com.divercity.android.data.entity.occupationofinterests

import com.google.gson.annotations.SerializedName

data class OOIResponse(

    @field:SerializedName("attributes")
    val attributes: Attributes? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    var isSelected: Boolean = false
)