package com.divercity.android.data.entity.activity.notification

import com.google.gson.annotations.SerializedName

data class Relationships(

    @field:SerializedName("activity_record_items_users")
	val activityRecordItemsUsers: ActivityRecordItemsUsers? = null,

    @field:SerializedName("a_target")
	val aTarget: ATarget? = null,

    @field:SerializedName("activity_record_items")
	val activityRecordItems: ActivityRecordItems? = null
)