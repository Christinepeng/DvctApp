package com.divercity.android.data.entity.activity.notificationread

import com.google.gson.annotations.SerializedName

data class NotificationReadBody(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null
)