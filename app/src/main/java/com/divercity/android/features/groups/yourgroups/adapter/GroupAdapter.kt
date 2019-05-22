package com.divercity.android.features.groups.yourgroups.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.groups.adapter.GroupsViewHolder
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class GroupAdapter @Inject
constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<GroupResponse> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: GroupsViewHolder.Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GroupsViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GroupsViewHolder).bindTo(position, list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}