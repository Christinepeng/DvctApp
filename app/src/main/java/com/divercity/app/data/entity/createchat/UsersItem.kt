package com.divercity.app.data.entity.createchat

import com.google.gson.annotations.SerializedName

data class UsersItem(

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("id")
	var id: String? = null
){
	constructor() : this("", "0")
}