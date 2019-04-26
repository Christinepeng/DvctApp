package com.divercity.android.data.entity.chat.messages

import com.google.gson.annotations.SerializedName

data class DataEntity(

    @field:SerializedName("subscriptions")
        val subscriptions: List<SubscriptionsItem?>? = null,

    @field:SerializedName("chats")
        val chats: List<ChatMessageEntityResponse>? = null
)