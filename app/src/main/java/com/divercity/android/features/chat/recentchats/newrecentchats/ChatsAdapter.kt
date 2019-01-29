package com.divercity.android.features.chat.recentchats.newrecentchats

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.divercity.android.R
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

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_text
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