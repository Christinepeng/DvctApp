package com.divercity.android.data.entity.activity.notificationread

import com.google.gson.annotations.SerializedName

data class DataItem(

	@field:SerializedName("record_ids")
	val recordIds: List<String?>? = null,

	@field:SerializedName("type")
	val type: String? = null
)