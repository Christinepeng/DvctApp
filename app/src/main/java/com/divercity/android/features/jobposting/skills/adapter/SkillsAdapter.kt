package com.divercity.android.features.jobposting.skills.adapter

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.divercity.android.R
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.ui.NetworkStateViewHolder
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.entity.skills.SkillResponse
import javax.inject.Inject

class SkillsAdapter @Inject
constructor() : PagedListAdapter<SkillResponse, RecyclerView.ViewHolder>(userDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null
    private var skillsCurrentSelected = ArrayList<SkillResponse>()
    lateinit var skillsPreviousSelected: ArrayList<SkillResponse>

    private var listener: SkillsViewHolder.Listener = object : SkillsViewHolder.Listener {

        override fun onSkillSelectUnselect(skill: SkillResponse) {
            if (skill.isSelected)
                skillsCurrentSelected.add(skill)
            else
                skillsCurrentSelected.remove(skill)
        }
    }

    fun getMergeListSelected() : ArrayList<SkillResponse>{
        skillsCurrentSelected.addAll(skillsPreviousSelected)
        return skillsCurrentSelected
    }

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_btn_text -> SkillsViewHolder.create(parent, listener)
            R.layout.view_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_btn_text -> {
                val skill = getItem(position)
                skillsPreviousSelected.let { skillsSelected ->
                    skill?.let { skill ->
                        if (skillsSelected.contains(skill)) {
                            skill.isSelected = true
                            skillsSelected.remove(skill)
                            skillsCurrentSelected.add(skill)
                        }
                    }
                }
                (holder as SkillsViewHolder).bindTo(skill)
            }
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
            R.layout.item_btn_text
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

        private val userDiffCallback = object : DiffUtil.ItemCallback<SkillResponse>() {

            override fun areItemsTheSame(oldItem: SkillResponse, newItem: SkillResponse): Boolean {
                return oldItem.id === newItem.id
            }

            override fun areContentsTheSame(oldItem: SkillResponse, newItem: SkillResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}