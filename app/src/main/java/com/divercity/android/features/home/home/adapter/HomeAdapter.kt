package com.divercity.android.features.home.home.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.ui.NetworkStateViewHolder
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.entity.home.HomeItem
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.groups.adapter.EmptyViewHolder
import com.divercity.android.features.home.home.adapter.recommended.RecommendedConnectionsSectionViewHolder
import com.divercity.android.features.home.home.adapter.recommended.RecommendedViewHolder
import com.divercity.android.features.home.home.adapter.viewholder.JobFeedViewHolder
import com.divercity.android.features.home.home.adapter.viewholder.QuestionsViewHolder
import com.divercity.android.features.home.home.adapter.viewholder.WritePostViewHolder
import com.divercity.android.model.Question
import com.divercity.android.repository.session.SessionRepository
import javax.inject.Inject

class HomeAdapter @Inject
constructor(
    val sessionRepository: SessionRepository
) : PagedListAdapter<HomeItem, RecyclerView.ViewHolder>(userDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null
    var feedJobListener: JobFeedViewHolder.Listener? = null
    var questionListener: QuestionsViewHolder.Listener? = null
    var adapterProxy: AdapterDataObserverProxy? = null
    lateinit var onWritePost: () -> Unit

    lateinit var recommendedAdapter: RecommendedAdapter
    lateinit var recommendedConnectionsAdapter: RecommendedConnectionsAdapter

    var showRecommendedSection = false

    companion object {

        private const val AMOUNT_HEADERS = 2

        private val userDiffCallback = object : DiffUtil.ItemCallback<HomeItem>() {

            override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_network_state -> NetworkStateViewHolder.create(
                parent,
                retryCallback
            )
            R.layout.item_question -> QuestionsViewHolder.create(
                parent,
                questionListener,
                sessionRepository
            )
            R.layout.item_feed_job -> JobFeedViewHolder.create(
                parent,
                feedJobListener,
                sessionRepository.isLoggedUserJobSeeker()
            )
            R.layout.item_list_recommended -> RecommendedViewHolder.create(
                parent,
                recommendedAdapter
            )
            R.layout.item_list_recommended_connection -> RecommendedConnectionsSectionViewHolder.create(
                parent,
                recommendedConnectionsAdapter
            )
            R.layout.view_empty -> EmptyViewHolder.create(parent)
            R.layout.view_write_post -> WritePostViewHolder.create(parent, onWritePost)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.view_network_state -> (holder as NetworkStateViewHolder)
                .bindTo(networkState)
            R.layout.item_question -> (holder as QuestionsViewHolder)
                .bindTo(position, getItem(position - getRightPosition(position)) as Question)
            R.layout.item_feed_job -> (holder as JobFeedViewHolder)
                .bindTo(position, getItem(position - getRightPosition(position)) as JobResponse)
            R.layout.item_list_recommended -> (holder as RecommendedViewHolder).bindTo(
                recommendedAdapter.currentList?.size
            )
            R.layout.item_list_recommended_connection -> (holder as RecommendedConnectionsSectionViewHolder).bindTo()
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState !== NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            if (showRecommendedSection)
                R.layout.item_list_recommended
            else
                R.layout.view_empty
        } else if (currentList?.isNotEmpty() == true && position == 1) {
            R.layout.view_write_post
        } else if (currentList?.isNotEmpty() == true && position == 4) {
            R.layout.item_list_recommended_connection
        } else if (hasExtraRow() && position == itemCount - 1) {
            R.layout.view_network_state
        } else if (getItem(position - getRightPosition(position)) is Question) {
            R.layout.item_question
        } else {
            R.layout.item_feed_job
        }
    }


    fun getRightPosition(position: Int) = if (position < 5) AMOUNT_HEADERS else 3

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

    fun onRefreshRetry() {
//        adapterProxy?.headerCount = 0
    }

    fun updatePositionOnJobApplied(position: Int) {
        (getItem(position - 1) as JobResponse).attributes?.apply {
            isAppliedByCurrent = true
            notifyItemChanged(position)
        }
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        adapterProxy = AdapterDataObserverProxy(observer)
        super.registerAdapterDataObserver(adapterProxy!!)
    }
}