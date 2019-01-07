package com.divercity.app.features.chat.chat.adapter

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.divercity.app.R
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.data.entity.chat.messages.ChatMessageResponse
import com.divercity.app.repository.user.UserRepository
import javax.inject.Inject

class ChatAdapter @Inject
constructor(val userRepository: UserRepository) :
    PagedListAdapter<ChatMessageResponse, RecyclerView.ViewHolder>(userDiffCallback) {

    private var retryCallback: RetryCallback? = null

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (itemCount > position + 1)
            (holder as ChatViewHolder).bindTo(userRepository.getUserId()!!, getItem(position), getItem(position + 1))
        else
            (holder as ChatViewHolder).bindTo(userRepository.getUserId()!!, getItem(position), null)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_text
    }

    companion object {

        private val userDiffCallback = object : DiffUtil.ItemCallback<ChatMessageResponse>() {

            override fun areItemsTheSame(oldItem: ChatMessageResponse, newItem: ChatMessageResponse): Boolean {
                return oldItem.id === newItem.id
            }

            override fun areContentsTheSame(oldItem: ChatMessageResponse, newItem: ChatMessageResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}