package com.divercity.android.features.company.companiesmycompanies.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.ui.NetworkStateViewHolder
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.companies.adapter.TabCompaniesViewHolder
import com.divercity.android.features.groups.adapter.EmptyViewHolder
import com.divercity.android.features.home.home.adapter.AdapterDataObserverProxy
import javax.inject.Inject

class CompanyMyCompaniesAdapter @Inject
constructor() :
    PagedListAdapter<CompanyResponse, RecyclerView.ViewHolder>(userDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null

    private var listener: TabCompaniesViewHolder.Listener? = null
    var myCompaniesListener: MyCompaniesViewHolder.Listener? = null
    private var adapterProxy: AdapterDataObserverProxy? = null

    lateinit var myCompaniesAdapter: CompanyAdapter

    var showMyCompaniesSection = false

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    fun setListener(listener: TabCompaniesViewHolder.Listener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_company -> TabCompaniesViewHolder.create(parent, listener)
            R.layout.view_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            R.layout.view_my_companies -> MyCompaniesViewHolder.create(
                parent,
                myCompaniesAdapter,
                myCompaniesListener
            )
            R.layout.view_empty -> EmptyViewHolder.create(parent)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_company -> (holder as TabCompaniesViewHolder).bindTo(
                getItem(position - 1)
            )
            R.layout.view_network_state -> (holder as NetworkStateViewHolder).bindTo(networkState)
            R.layout.view_my_companies -> (holder as MyCompaniesViewHolder).bindTo(
                myCompaniesAdapter.list.size
            )
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState !== NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            if (showMyCompaniesSection)
                R.layout.view_my_companies
            else
                R.layout.view_empty
        } else if (hasExtraRow() && position == itemCount - 1) {
            R.layout.view_network_state
        } else {
            R.layout.item_company
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
            object : DiffUtil.ItemCallback<CompanyResponse>() {

                override fun areItemsTheSame(
                    oldItem: CompanyResponse,
                    newItem: CompanyResponse
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: CompanyResponse,
                    newItem: CompanyResponse
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