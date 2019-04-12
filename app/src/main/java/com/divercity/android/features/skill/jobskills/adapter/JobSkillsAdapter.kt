package com.divercity.android.features.skill.jobskills.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.ui.NetworkStateViewHolder
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.features.skill.jobskills.model.SkillSelectedModel
import javax.inject.Inject

class JobSkillsAdapter @Inject
constructor() : PagedListAdapter<SkillResponse, RecyclerView.ViewHolder>(userDiffCallback) {

    private var networkState: NetworkState? = null
    private var retryCallback: RetryCallback? = null
    var skillsSelected = ArrayList<SkillResponse>()

    private var listener: JobSkillsViewHolder.Listener = object : JobSkillsViewHolder.Listener {

        override fun onSkillSelectUnselect(skill: SkillSelectedModel) {
            if (skill.isSelected)
                skillsSelected.add(skill.data)
            else
                skillsSelected.remove(skill.data)
        }
    }

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_btn_text -> JobSkillsViewHolder.create(parent, listener)
            R.layout.view_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_btn_text -> {
                skillsSelected.let { skillsSelected ->
                    getItem(position)?.let { skill ->
                        val skillSelectedModel = SkillSelectedModel(skill, false)
                        skillsSelected.forEach {
                            if (it.id == skill.id) {
                                skillSelectedModel.isSelected = true
                                return@forEach
                            }
                        }
                        (holder as JobSkillsViewHolder).bindTo(skillSelectedModel)
                    }
                }
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

            override fun areItemsTheSame(
                oldItem: SkillResponse,
                newItem: SkillResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SkillResponse,
                newItem: SkillResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}