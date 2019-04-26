package com.divercity.android.features.profile.useradapter.charpaginationsinglesel

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.ui.NetworkStateViewHolder
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.entity.user.response.UserEntityResponse
import com.divercity.android.features.profile.useradapter.charpagination.CharacterViewHolder
import javax.inject.Inject

// It is Any because it has a list of Characters and Names. Character is to get the segmented
// list by firs char.
class UserCharPagSingleSelAdapter @Inject
constructor() : PagedListAdapter<Any, RecyclerView.ViewHolder>(userDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null
    var lastPositionSelected: Int? = null

    private var listener = object :
        UserSingleSelViewHolder.Listener {

        override fun onSelectUnselectUser(position: Int, isSelected: Boolean) {
            if (isSelected) {
                if (lastPositionSelected != null) {
                    val tempPos = lastPositionSelected!!
                    lastPositionSelected = position
                    notifyItemChanged(tempPos)
                } else {
                    lastPositionSelected = position
                }
            } else {
                lastPositionSelected = null
            }
        }
    }

    fun getUserSelected(): UserEntityResponse? {
        return if (lastPositionSelected != null)
            getItem(lastPositionSelected!!) as UserEntityResponse
        else
            null
    }

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_user_action -> UserSingleSelViewHolder.create(
                parent,
                listener
            )
            R.layout.view_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            R.layout.item_contact_character -> CharacterViewHolder.create(
                parent
            )
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_user_action -> (holder as UserSingleSelViewHolder)
                .bindTo(
                    lastPositionSelected == position,
                    getItem(position) as UserEntityResponse,
                    position
                )
            R.layout.view_network_state -> (holder as NetworkStateViewHolder).bindTo(networkState)
            R.layout.item_contact_character -> (holder as CharacterViewHolder)
                .bindTo(
                    getItem(
                        position
                    ) as Char
                )
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.view_network_state
        } else if (getItem(position) is UserEntityResponse) {
            R.layout.item_user_action
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