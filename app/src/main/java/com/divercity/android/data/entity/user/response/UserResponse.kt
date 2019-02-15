package com.divercity.android.data.entity.user.response

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.TypeConverters
import com.divercity.android.db.converter.CompanyTypeConverter
import com.divercity.android.db.converter.IntListTypeConverter
import com.divercity.android.db.converter.StringListTypeConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user",primaryKeys = ["id"])
@TypeConverters(
	CompanyTypeConverter::class,
	IntListTypeConverter::class,
	StringListTypeConverter::class)
data class UserResponse(

	@field:SerializedName("attributes")
	@Embedded( prefix = "attr_" )
	var userAttributes: UserAttributes? = null,

	@field:SerializedName("id")
	var id: String = "-1",

	@field:SerializedName("type")
	var type: String? = "users"
)