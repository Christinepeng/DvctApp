package com.divercity.app.data.entity.createchat

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class CreateChatResponse(

	@field:SerializedName("current_user_chat")
	val currentUserChat: CurrentUserChat? = null,

	@field:SerializedName("other_user")
	val otherUser: Int? = null
)