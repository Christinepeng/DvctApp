package com.divercity.android.features.invitations.users.adapter
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.ui.NetworkStateViewHolder
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.profile.useradapter.charpagination.CharacterViewHolder
import javax.inject.Inject

class UserSelectionAdapter @Inject
constructor() : PagedListAdapter<Any, RecyclerView.ViewHolder>(userDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null
    var selectedUsers = HashSet<UserResponse>()

    private var listener = object : UserSelectionViewHolder.Listener {

        override fun onSelectUnselectUser(user: UserResponse, isSelected: Boolean) {
            if (isSelected)
                selectedUsers.add(user)
            else
                selectedUsers.remove(user)
        }
    }

    fun getSelectedUsersIds() : List<String>{
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
            R.layout.item_user_selection -> UserSelectionViewHolder.create(parent, listener)
            R.layout.view_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            R.layout.item_contact_character -> CharacterViewHolder.create(parent)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_user_selection -> (holder as UserSelectionViewHolder).bindTo(
                selectedUsers.contains(getItem(position)),
                getItem(position) as UserResponse
            )
            R.layout.view_network_state -> (holder as NetworkStateViewHolder).bindTo(networkState)
            R.layout.item_contact_character -> (holder as CharacterViewHolder).bindTo(getItem(position) as Char)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.view_network_state
        } else if (getItem(position) is UserResponse) {
            R.layout.item_user_selection
        } else {
            R.layout.item_contact_character
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

        private val userDiffCallback = object : DiffUtil.ItemCallback<Any>() {

            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem == newItem
            }
        }
    }
}