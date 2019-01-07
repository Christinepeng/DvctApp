package com.divercity.app.data.entity.chat.messages

import com.google.gson.annotations.SerializedName

data class Data(

    @field:SerializedName("subscriptions")
        val subscriptions: List<SubscriptionsItem?>? = null,

    @field:SerializedName("chats")
        val chats: List<ChatMessageResponse>? = null
)