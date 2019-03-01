package com.divercity.android.data.entity.activity.notification

import com.google.gson.annotations.SerializedName

data class ActivityRecordItems(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null
)