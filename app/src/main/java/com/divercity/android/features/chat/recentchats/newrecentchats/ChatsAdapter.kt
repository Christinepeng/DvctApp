package com.divercity.android.features.chat.recentchats.newrecentchats

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.repository.user.UserRepository
import javax.inject.Inject

class ChatsAdapter @Inject
constructor(val userRepository: UserRepository) :
    PagedListAdapter<ExistingUsersChatListItem, RecyclerView.ViewHolder>(userDiffCallback) {

    var listener: RecentChatViewHolder.Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecentChatViewHolder.create(parent,listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecentChatViewHolder).bindTo(getItem(position))
    }

    companion object {

        private val userDiffCallback = object : DiffUtil.ItemCallback<ExistingUsersChatListItem>() {

            override fun areItemsTheSame(oldItem: ExistingUsersChatListItem, newItem: ExistingUsersChatListItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ExistingUsersChatListItem, newItem: ExistingUsersChatListItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}