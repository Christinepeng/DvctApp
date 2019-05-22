package com.divercity.android.features.groups.yourgroups.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.ui.NetworkStateViewHolder
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.features.groups.adapter.EmptyViewHolder
import com.divercity.android.features.home.home.adapter.AdapterDataObserverProxy
import com.divercity.android.features.home.home.adapter.viewholder.QuestionsViewHolder
import com.divercity.android.model.Question
import com.divercity.android.repository.session.SessionRepository
import javax.inject.Inject

class YourGroupsAdapter @Inject
constructor(val sessionRepository: SessionRepository) :
    PagedListAdapter<Question, RecyclerView.ViewHolder>(userDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null

    var questionListener: QuestionsViewHolder.Listener? = null
    var yourGroupsListener: YourGroupsViewHolder.Listener? = null
    private var adapterProxy: AdapterDataObserverProxy? = null

    lateinit var groupAdapter: GroupAdapter

    var showFollowedGroupSection = false

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_question -> QuestionsViewHolder.create(
                parent,
                questionListener,
                sessionRepository
            )
            R.layout.view_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            R.layout.view_your_groups -> YourGroupsViewHolder.create(
                parent,
                groupAdapter,
                yourGroupsListener
            )
            R.layout.view_empty -> EmptyViewHolder.create(parent)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_question -> (holder as QuestionsViewHolder).bindTo(
                position,
                getItem(position - 1)
            )
            R.layout.view_network_state -> (holder as NetworkStateViewHolder).bindTo(networkState)
            R.layout.view_your_groups -> (holder as YourGroupsViewHolder).bindTo(
                groupAdapter.list.size
            )
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState !== NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            if (showFollowedGroupSection)
                R.layout.view_your_groups
            else
                R.layout.view_empty
        } else if (hasExtraRow() && position == itemCount - 1) {
            R.layout.view_network_state
        } else {
            R.layout.item_question
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1 + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(itemCount - 1)
            } else {
                notifyItemInserted(itemCount - 1)
            }
        } else if (hasExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(itemCount - 2)
        }
    }

    companion object {

        private val userDiffCallback =
            object : DiffUtil.ItemCallback<Question>() {

                override fun areItemsTheSame(
                    oldItem: Question,
                    newItem: Question
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Question,
                    newItem: Question
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        adapterProxy = AdapterDataObserverProxy(observer)
        super.registerAdapterDataObserver(adapterProxy!!)
    }
}