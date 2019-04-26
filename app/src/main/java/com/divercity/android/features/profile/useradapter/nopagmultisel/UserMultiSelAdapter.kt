package com.divercity.android.features.profile.useradapter.nopagmultisel

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.features.profile.useradapter.charpaginationmultiplesel.UserMultipleSelViewHolder
import com.divercity.android.model.user.User
import javax.inject.Inject

class UserMultiSelAdapter @Inject
constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<User>? = null

    var selectedUsers = HashSet<User>()

    fun setData(list : HashSet<User>) {
        selectedUsers = list
        data = ArrayList(list)
        notifyDataSetChanged()
    }

    var listener = object : UserMultipleSelViewHolder.Listener {

        override fun onSelectUnselectUser(user: User, isSelected: Boolean) {
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
        return UserMultipleSelViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data?.get(position)
        (holder as UserMultipleSelViewHolder).bindTo(selectedUsers.contains(item), item)
    }
}