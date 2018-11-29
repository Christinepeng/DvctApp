package com.divercity.app.data.entity.group.recommendedgroups

import com.google.gson.annotations.SerializedName

data class RecommendationItem(

        @field:SerializedName("story_type")
        val storyType: String? = null,

        @field:SerializedName("followers_count")
        var followersCount: Int? = null,

        @field:SerializedName("questions_count")
        val questionsCount: Int? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("picture_main")
        val pictureMain: String? = null,

        var isIsFollowedByCurrent: Boolean = false
)