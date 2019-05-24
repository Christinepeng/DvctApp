package com.divercity.android.features.chat.recentchats.oldrecentchats

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.ui.NetworkStateViewHolder
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.features.chat.recentchats.newrecentchats.RecentChatViewHolder
import com.divercity.android.repository.user.UserRepository
import javax.inject.Inject

class RecentChatAdapter @Inject
constructor(val userRepository: UserRepository) :
    PagedListAdapter<ExistingUsersChatListItem, RecyclerView.ViewHolder>(chatDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null
    private var listener: RecentChatViewHolder.Listener? = null

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    fun setListener(listener: RecentChatViewHolder.Listener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_recent_chat -> RecentChatViewHolder.create(
                parent,
                listener
            )
            R.layout.view_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_recent_chat -> (holder as RecentChatViewHolder).bindTo(getItem(position))
            R.layout.view_network_state -> (holder as NetworkStateViewHolder).bindTo(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState !== NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.view_network_state
        } else {
            R.layout.item_recent_chat
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun getNetworkState() : NetworkState? = networkState

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (hasExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {

        private val chatDiffCallback = object : DiffUtil.ItemCallback<ExistingUsersChatListItem>() {

            override fun areItemsTheSame(
                oldItem: ExistingUsersChatListItem,
                newItem: ExistingUsersChatListItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ExistingUsersChatListItem,
                newItem: ExistingUsersChatListItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}