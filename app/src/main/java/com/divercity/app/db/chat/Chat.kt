package com.divercity.app.db.chat

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "chat",primaryKeys = ["id"])
data class Chat(

    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("other_user_id")
    var otherUserId: Int? = null
) {
    constructor() : this(
        -1, -1
    )
}