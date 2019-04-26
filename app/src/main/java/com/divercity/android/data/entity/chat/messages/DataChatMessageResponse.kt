package com.divercity.android.data.entity.chat.messages

import com.google.gson.annotations.SerializedName

data class DataChatMessageResponse(

    @field:SerializedName("data")
    val data: DataEntity? = null,

    @field:SerializedName("meta")
    val meta: Meta? = null
)