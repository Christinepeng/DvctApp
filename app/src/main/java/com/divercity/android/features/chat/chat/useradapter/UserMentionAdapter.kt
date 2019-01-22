package com.divercity.android.features.chat.chat.useradapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.divercity.android.data.entity.user.response.UserResponse
import javax.inject.Inject

class UserMentionAdapter @Inject
constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<UserResponse>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: UserMentionViewHolder.Listener? = null

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserMentionViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as UserMentionViewHolder).bindTo(data?.get(position))
    }
}