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
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.home.RecommendedItem
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.groups.adapter.EmptyViewHolder
import com.divercity.android.features.home.home.adapter.recommended.RecommendedGroupViewHolder
import com.divercity.android.features.home.home.adapter.recommended.RecommendedJobViewHolder
import com.divercity.android.model.position.GroupPosition
import com.divercity.android.model.position.JobPosition
import com.divercity.android.repository.session.SessionRepository
import javax.inject.Inject

class RecommendedAdapter @Inject
constructor(val sessionRepository: SessionRepository) :
    PagedListAdapter<RecommendedItem, RecyclerView.ViewHolder>(userDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null
    var groupListener: RecommendedGroupViewHolder.Listener? = null
    var jobListener: RecommendedJobViewHolder.Listener? = null
//    var adapterProxy: AdapterDataObserverProxy? = null

    private var discardedGroups = ArrayList<String>()
    private var discardedJobs = ArrayList<String>()

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
            JOB -> RecommendedJobViewHolder.create(
                parent,
                jobListener,
                sessionRepository.isLoggedUserJobSeeker()
            )
            GROUP -> RecommendedGroupViewHolder.create(
                parent,
                groupListener
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
            JOB -> (holder as RecommendedJobViewHolder).bindTo(
                position,
                getItem(position) as JobResponse
            )
            GROUP -> (holder as RecommendedGroupViewHolder)
                .bindTo(GroupPosition(position, getItem(position) as GroupResponse))
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
        } else if (getItem(position) is JobResponse &&
            discardedJobs.contains((getItem(position) as JobResponse).id) ||
            (getItem(position) is GroupResponse) &&
            discardedGroups.contains((getItem(position) as GroupResponse).id)
        ) {
            R.layout.view_empty
        } else if (getItem(position) is JobResponse) {
            JOB
        } else
            GROUP
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
//                adapterProxy?.headerCount = 0
            } else {
                notifyItemInserted(itemCount)
//                adapterProxy?.headerCount = 1
            }
        } else if (hasExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    fun onRefreshRetry() {
//        adapterProxy?.headerCount = 0
    }

    fun updatePositionOnJoinGroup(groupPos: GroupPosition) {
        groupPos.group.attributes.apply {
            followersCount += 1
            isFollowedByCurrent = true
            notifyItemChanged(groupPos.position)
        }
    }

    fun updatePositionOnJoinGroupRequest(groupPos: GroupPosition) {
        groupPos.group.attributes.apply {
            requestToJoinStatus = "pending"
            notifyItemChanged(groupPos.position)
        }
    }

    fun updatePositionOnJobApplied(position: Int) {
        (getItem(position) as JobResponse).attributes?.apply {
            isAppliedByCurrent = true
            notifyItemChanged(position)
        }
    }

    companion object {

        const val JOB = 100
        const val GROUP = 150

        private val userDiffCallback = object : DiffUtil.ItemCallback<RecommendedItem>() {

            override fun areItemsTheSame(
                oldItem: RecommendedItem,
                newItem: RecommendedItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RecommendedItem,
                newItem: RecommendedItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

//    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
//        adapterProxy = AdapterDataObserverProxy(observer)
//        super.registerAdapterDataObserver(adapterProxy!!)
//    }

    fun onGroupDiscarded(groupPos: GroupPosition) {
        discardedGroups.add(groupPos.group.id)
        notifyItemChanged(groupPos.position)
    }

    fun onJobDiscarded(jobPos: JobPosition) {
        discardedJobs.add(jobPos.job.id!!)
        notifyItemChanged(jobPos.position)
    }
}