package com.divercity.android.features.home.home.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.ui.NetworkStateHorizontalViewHolder
import com.divercity.android.core.ui.NetworkStateViewHolder
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.features.groups.adapter.EmptyViewHolder
import com.divercity.android.features.home.home.adapter.recommended.RecommendedConnectionViewHolder
import com.divercity.android.model.position.UserPosition
import com.divercity.android.model.user.User
import com.divercity.android.repository.session.SessionRepository
import javax.inject.Inject

class RecommendedConnectionsAdapter @Inject
constructor(val sessionRepository: SessionRepository) :
    PagedListAdapter<User, RecyclerView.ViewHolder>(userDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null
    var listener: RecommendedConnectionViewHolder.Listener? = null
    private var adapterProxy: AdapterDataObserverProxy? = null

    private var discarded = ArrayList<String>()

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_network_state -> NetworkStateViewHolder.create(
                parent,
                retryCallback
            )
            R.layout.view_network_state_horizontal -> NetworkStateHorizontalViewHolder.create(
                parent,
                retryCallback
            )
            R.layout.item_recommended_connection -> RecommendedConnectionViewHolder.create(
                parent,
                listener
            )
            R.layout.view_empty -> EmptyViewHolder.create(parent)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.view_network_state -> (holder as NetworkStateViewHolder)
                .bindTo(networkState)
            R.layout.view_network_state_horizontal -> (holder as NetworkStateHorizontalViewHolder)
                .bindTo(networkState)
            R.layout.item_recommended_connection -> (holder as RecommendedConnectionViewHolder).bindTo(
                position,
                getItem(position) as User
            )
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState !== NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            if (currentList != null && currentList?.size == 0)
                R.layout.view_network_state
            else
                R.layout.view_network_state_horizontal
        } else if (getItem(position) is User &&
            discarded.contains((getItem(position) as User).id)
        ) {
            R.layout.view_empty
        } else {
            R.layout.item_recommended_connection
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
                notifyItemRemoved(itemCount)
                adapterProxy?.headerCount = 0
            } else {
                notifyItemInserted(itemCount)
                adapterProxy?.headerCount = 1
            }
        } else if (hasExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    fun onRefreshRetry() {
        adapterProxy?.headerCount = 0
    }

    companion object {

        private val userDiffCallback = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        adapterProxy = AdapterDataObserverProxy(observer)
        super.registerAdapterDataObserver(adapterProxy!!)
    }

    fun onUserDiscarded(userPosition: UserPosition) {
        discarded.add(userPosition.user.id)
        notifyItemChanged(userPosition.position)
    }
}