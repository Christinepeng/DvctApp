package com.divercity.android.features.groups.createnewpost.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.ui.NetworkStateHorizontalViewHolder
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.entity.group.group.GroupResponse
import javax.inject.Inject

class GroupsSmallAdapter @Inject
constructor() : PagedListAdapter<GroupResponse, RecyclerView.ViewHolder>(userDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null

    var selectedGroups = HashSet<GroupResponse>()
    var onSelectUnselectGroup: ((ArrayList<GroupResponse>) -> Unit)? = null

    private var listener = object : GroupsSmallViewHolder.Listener {
        override fun onSelectUnselectGroup(group: GroupResponse, isSelected: Boolean) {
            if (isSelected)
                selectedGroups.add(group)
            else
                selectedGroups.remove(group)
            onSelectUnselectGroup?.invoke(ArrayList(selectedGroups))
        }
    }

    fun getSelectedGroupsIds(): List<String> {
        val usersId = ArrayList<String>()
        selectedGroups.forEach {
            usersId.add(it.id)
        }
        return usersId
    }

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_text_black -> GroupsSmallViewHolder.create(parent, listener)
            R.layout.view_network_state_horizontal -> NetworkStateHorizontalViewHolder.create(
                parent,
                retryCallback
            )
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_text_black -> (holder as GroupsSmallViewHolder).bindTo(
                selectedGroups.contains(getItem(position)),
                getItem(position)
            )
            R.layout.view_network_state_horizontal -> (holder as NetworkStateHorizontalViewHolder).bindTo(
                networkState
            )
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState !== NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.view_network_state_horizontal
        } else {
            R.layout.item_text_black
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {

        private val userDiffCallback = object : DiffUtil.ItemCallback<GroupResponse>() {

            override fun areItemsTheSame(oldItem: GroupResponse, newItem: GroupResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: GroupResponse,
                newItem: GroupResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}