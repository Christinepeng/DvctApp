package com.divercity.app.features.jobs.applicants.adapter

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.divercity.app.R
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.ui.NetworkStateViewHolder
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.data.entity.jobapplication.JobApplicationResponse
import javax.inject.Inject

class JobApplicantsAdapter @Inject
constructor() : PagedListAdapter<JobApplicationResponse, RecyclerView.ViewHolder>(userDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null
    private var listener: JobApplicantsViewHolder.Listener? = null

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    fun setListener(listener: JobApplicantsViewHolder.Listener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_user -> JobApplicantsViewHolder.create(parent, listener)
            R.layout.view_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_user -> (holder as JobApplicantsViewHolder).bindTo(position, getItem(position))
            R.layout.view_network_state -> (holder as NetworkStateViewHolder).bindTo(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState !== NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.view_network_state
        } else {
            R.layout.item_user
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

        private val userDiffCallback = object : DiffUtil.ItemCallback<JobApplicationResponse>() {

            override fun areItemsTheSame(oldItem: JobApplicationResponse, newItem: JobApplicationResponse): Boolean {
                return oldItem.id === newItem.id
            }

            override fun areContentsTheSame(oldItem: JobApplicationResponse, newItem: JobApplicationResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}