package com.divercity.android.features.home.home.adapter.recommended

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.home.RecommendedItem
import com.divercity.android.data.entity.job.response.JobResponse
import javax.inject.Inject

class RecommendedAdapter @Inject
constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<RecommendedItem>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var groupListener: RecommendedGroupViewHolder.Listener? = null
    var jobListener: RecommendedJobViewHolder.Listener? = null

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GROUP -> RecommendedGroupViewHolder.create(parent, groupListener)
            else -> RecommendedJobViewHolder.create(parent, jobListener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            JOB -> (holder as RecommendedJobViewHolder).bindTo(position, data?.get(position) as JobResponse)
            GROUP -> (holder as RecommendedGroupViewHolder).bindTo(position, data?.get(position) as GroupResponse)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data?.get(position) is JobResponse) {
            JOB
        } else {
            GROUP
        }
    }

    fun updatePositionOnJoinGroup(position: Int){
        (data?.get(position) as GroupResponse).attributes?.apply {
            followersCount += 1
            isFollowedByCurrent = true
            notifyItemChanged(position)
        }
    }

    fun updatePositionOnJoinRequest(position: Int){
        // TODO: update with response group data
        (data?.get(position) as GroupResponse).attributes?.apply {
            requestToJoinStatus = "pending"
            notifyItemChanged(position)
        }
    }

    companion object {

        const val JOB = 100
        const val GROUP = 150
    }
}