package com.divercity.android.data.entity.activity.notification

import com.google.gson.annotations.SerializedName

data class NotificationResponse(

    @field:SerializedName("relationships")
	val relationships: Relationships? = null,

    @field:SerializedName("attributes")
	val attributes: Attributes? = null,

    @field:SerializedName("id")
	val id: String? = null,

    @field:SerializedName("type")
	val type: String? = null
)