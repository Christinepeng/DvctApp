package com.divercity.android.features.groups.createnewpost.adapter.selected

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.data.entity.group.group.GroupResponse
import javax.inject.Inject

class GroupSelectedAdapter @Inject
constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var groups: ArrayList<GroupResponse>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun getGroupsIds() : List<String>{
        val groupsId = ArrayList<String>()
        groups?.forEach {
            groupsId.add(it.id)
        }
        return groupsId
    }

    var listener: GroupSelectedViewHolder.Listener? = null

    override fun getItemCount(): Int {
        return groups?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GroupSelectedViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GroupSelectedViewHolder).bindTo(groups?.get(position))
    }
}