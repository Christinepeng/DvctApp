package com.divercity.android.data.entity.industry

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IndustryResponse(

    @field:SerializedName("attributes")
	val attributes: Attributes? = null,

    @field:SerializedName("id")
	val id: String? = null,

    @field:SerializedName("type")
	val type: String? = null
) : Parcelable