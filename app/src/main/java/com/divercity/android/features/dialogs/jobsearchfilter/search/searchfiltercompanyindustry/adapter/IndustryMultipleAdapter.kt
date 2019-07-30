package com.divercity.android.features.dialogs.jobsearchfilter.search.searchfiltercompanyindustry.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.ui.NetworkStateViewHolder
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.entity.industry.IndustryResponse
import javax.inject.Inject

class IndustryMultipleAdapter @Inject
constructor() : PagedListAdapter<IndustryResponse, RecyclerView.ViewHolder>(userDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null
    var selectedIndustries = HashSet<IndustryResponse>()

    var onSelectUnselectIndustry: (() -> Unit)? = null

    private var listener = object : IndustryMultipleViewHolder.Listener {

        override fun onSelectUnselectIndustry(industry: IndustryResponse, isSelected: Boolean) {
            if (isSelected)
                selectedIndustries.add(industry)
            else
                selectedIndustries.remove(industry)
            onSelectUnselectIndustry?.invoke()
        }
    }

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    fun onSelectAll() {
        selectedIndustries.clear()
        notifyDataSetChanged()
    }

    fun getSelectedIndustriesString(): String {
        return selectedIndustries.joinToString {
            it.attributes?.name ?: ""
        }
    }

    fun getSelectedIndustriesIds(): List<String> {
        return selectedIndustries.map {
            it.id!!
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_text -> IndustryMultipleViewHolder.create(
                parent,
                listener
            )
            R.layout.view_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_text -> (holder as IndustryMultipleViewHolder).bindTo(
                selectedIndustries.contains(getItem(position)),
                getItem(position)
            )
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
            R.layout.item_text
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

        private val userDiffCallback = object : DiffUtil.ItemCallback<IndustryResponse>() {

            override fun areItemsTheSame(
                oldItem: IndustryResponse,
                newItem: IndustryResponse
            ): Boolean {
                return oldItem.id === newItem.id
            }

            override fun areContentsTheSame(
                oldItem: IndustryResponse,
                newItem: IndustryResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}