package com.divercity.android.data.entity.company.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CompanyResponse(

	@field:SerializedName("attributes")
	var attributes: Attributes? = null,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("type")
	val type: String? = null
) : Parcelable