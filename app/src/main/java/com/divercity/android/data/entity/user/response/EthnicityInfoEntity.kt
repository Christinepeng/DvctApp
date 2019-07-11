package com.divercity.android.data.entity.user.response

import com.google.gson.annotations.SerializedName

data class EthnicityInfoEntity(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String
)