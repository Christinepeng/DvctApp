package com.divercity.app.data.entity.recommendedgroups

import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("current_user_admin_level")
	val currentUserAdminLevel: String? = null,

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("questions_count")
	val questionsCount: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Int? = null,

	@field:SerializedName("group_type")
	val groupType: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("is_current_user_admin")
	val isCurrentUserAdmin: Boolean? = null,

	@field:SerializedName("answers_count")
	val answersCount: Int? = null,

	@field:SerializedName("followers_count")
	var followersCount: Int? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("is_followed_by_current")
	var isFollowedByCurrent: Boolean? = null,

	@field:SerializedName("picture_main")
	val pictureMain: String? = null,

	@field:SerializedName("author_info")
	val authorInfo: AuthorInfo? = null,

	@field:SerializedName("request_to_join_status")
	val requestToJoinStatus: String? = null
)