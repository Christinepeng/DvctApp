package com.divercity.android.features.chat.creategroupchat.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.chat.newgroupchat.adapter.UserMultipleViewHolder
import javax.inject.Inject

class UserGroupMemberAdapter @Inject
constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<UserResponse>? = null

    var selectedUsers = HashSet<UserResponse>()

    fun setData(list : HashSet<UserResponse>) {
        selectedUsers = list
        data = ArrayList(list)
        notifyDataSetChanged()
    }

    var listener = object : UserMultipleViewHolder.Listener {

        override fun onSelectUnselectUser(user: UserResponse, isSelected: Boolean) {
            if (isSelected)
                selectedUsers.add(user)
            else
                selectedUsers.remove(user)
        }
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserMultipleViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data?.get(position)
        (holder as UserMultipleViewHolder).bindTo(selectedUsers.contains(item), item)
    }
}