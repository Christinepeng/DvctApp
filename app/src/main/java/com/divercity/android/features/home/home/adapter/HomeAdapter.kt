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
import com.divercity.android.data.entity.home.Recommended
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.data.entity.questions.QuestionResponse
import com.divercity.android.features.home.home.adapter.recommended.RecommendedAdapter
import com.divercity.android.features.home.home.adapter.viewholder.JobFeedViewHolder
import com.divercity.android.features.home.home.adapter.viewholder.QuestionsViewHolder
import com.divercity.android.features.home.home.adapter.viewholder.RecommendedViewHolder
import com.divercity.android.features.jobs.jobs.adapter.JobsViewHolder
import com.divercity.android.repository.session.SessionRepository
import javax.inject.Inject

class HomeAdapter @Inject
constructor(val sessionRepository: SessionRepository) :
    PagedListAdapter<HomeItem, RecyclerView.ViewHolder>(userDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null
    lateinit var recommendedAdapter: RecommendedAdapter
    var feedJobListener: JobsViewHolder.Listener? = null
    var questionListener: QuestionsViewHolder.Listener? = null

    var adapterProxy: AdapterDataObserverProxy? = null

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_question -> QuestionsViewHolder.create(
                parent,
                sessionRepository,
                questionListener
            )
            R.layout.view_network_state -> NetworkStateViewHolder.create(
                parent,
                retryCallback
            )
            R.layout.item_list_recommended -> RecommendedViewHolder.create(parent)
            R.layout.item_feed_job -> JobFeedViewHolder.create(
                parent,
                feedJobListener,
                sessionRepository.isLoggedUserJobSeeker()
            )
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_question -> (holder as QuestionsViewHolder)
                .bindTo(getItem(position) as QuestionResponse)
            R.layout.view_network_state -> (holder as NetworkStateViewHolder)
                .bindTo(networkState)
            R.layout.item_list_recommended -> (holder as RecommendedViewHolder)
                .bindTo(getItem(position) as Recommended?, recommendedAdapter)
            R.layout.item_feed_job -> (holder as JobFeedViewHolder)
                .bindTo(position, getItem(position) as JobResponse)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.view_network_state
        } else if (position == 0) {
            R.layout.item_list_recommended
        } else if (getItem(position) is QuestionResponse) {
            R.layout.item_question
        } else
            R.layout.item_feed_job
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
                adapterProxy?.headerCount = 0
            } else {
                notifyItemInserted(super.getItemCount())
                adapterProxy?.headerCount = 1
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {

        private val userDiffCallback = object : DiffUtil.ItemCallback<HomeItem>() {

            override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        adapterProxy = AdapterDataObserverProxy(observer)
        super.registerAdapterDataObserver(adapterProxy!!)
    }
}