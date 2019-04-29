package com.divercity.android.features.user.useradapter.groupadmin

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.ui.NetworkStateViewHolder
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.model.user.User
import com.divercity.android.repository.session.SessionRepository
import javax.inject.Inject

class GroupAdminAdapter @Inject
constructor(val sessionRepository: SessionRepository) :
    PagedListAdapter<User, RecyclerView.ViewHolder>(userDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null
    var selectedUsers = HashSet<User>()
    var onSelectUnselectUser: ((ArrayList<User>) -> Unit)? = null

    lateinit var ownerId: String

    private var listener = object : GroupAdminViewHolder.Listener {

        override fun onSelectUnselectUser(user: User, isSelected: Boolean) {
            if (isSelected)
                selectedUsers.add(user)
            else
                selectedUsers.remove(user)
            onSelectUnselectUser?.invoke(ArrayList(selectedUsers))
        }
    }

    fun getSelectedUsersIds(): List<String> {
        val usersId = ArrayList<String>()
        selectedUsers.forEach {
            usersId.add(it.id)
        }
        return usersId
    }

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_user_action -> GroupAdminViewHolder.create(
                parent,
                listener,
                ownerId,
                sessionRepository.getUserId()
            )
            R.layout.view_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_user_action -> (holder as GroupAdminViewHolder).bindTo(
                selectedUsers.contains(getItem(position)),
                getItem(position) as User
            )
            R.layout.view_network_state -> (holder as NetworkStateViewHolder).bindTo(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.view_network_state
        } else {
            R.layout.item_user_action
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

        private val userDiffCallback = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }
}