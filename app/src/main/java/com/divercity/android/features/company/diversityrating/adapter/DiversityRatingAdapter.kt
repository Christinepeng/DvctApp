package com.divercity.android.features.company.diversityrating.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.ui.NetworkStateViewHolder
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.home.home.adapter.AdapterDataObserverProxy
import com.divercity.android.model.CompanyDiversityReview
import javax.inject.Inject

class DiversityRatingAdapter @Inject
constructor() :
    PagedListAdapter<CompanyDiversityReview, RecyclerView.ViewHolder>(userDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null

    private var company: CompanyResponse? = null

    lateinit var proxy: AdapterDataObserverProxy

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_review -> DiversityRatingReviewViewHolder.create(parent)
            R.layout.view_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            R.layout.view_all_ratings -> DiversityRatingViewHolder.create(parent)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_review -> (holder as DiversityRatingReviewViewHolder).bindTo(
                getItem(position - COUNT_HEADER)
            )
            R.layout.view_all_ratings -> {
                (holder as DiversityRatingViewHolder).bindTo(company)
            }
            R.layout.view_network_state -> (holder as NetworkStateViewHolder).bindTo(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == HEADER_POSITION) {
            R.layout.view_all_ratings
        } else if (hasExtraRow() && position == itemCount - 1) {
            R.layout.view_network_state
        } else {
            R.layout.item_review
        }
    }

    fun setCompany(c: CompanyResponse?) {
        company?.let {
            company = c
        } ?: run {
            company = c
            notifyItemChanged(HEADER_POSITION - COUNT_HEADER)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + COUNT_HEADER + if (hasExtraRow()) 1 else 0
    }

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
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 2)
        }
    }

    companion object {

        const val HEADER_POSITION = 0
        const val COUNT_HEADER = 1

        private val userDiffCallback =
            object : DiffUtil.ItemCallback<CompanyDiversityReview>() {

                override fun areItemsTheSame(
                    oldItem: CompanyDiversityReview,
                    newItem: CompanyDiversityReview
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: CompanyDiversityReview,
                    newItem: CompanyDiversityReview
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        proxy = AdapterDataObserverProxy(observer)
        super.registerAdapterDataObserver(proxy)
    }
}