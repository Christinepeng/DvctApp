package com.divercity.android.features.groups.selectfollowedgroup.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.android.R
import com.divercity.android.data.entity.group.group.GroupResponse
import kotlinx.android.synthetic.main.item_text_black.view.*

class GroupsSimpleViewHolder
private constructor(itemView: View, private val listener: Listener?) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: GroupResponse?) {
        data?.let {
            itemView.apply {

                txt_name.text = it.attributes.title

                setOnClickListener {
                    listener?.onGroupClick(data)
                }
            }
        }
    }

    interface Listener {

        fun onGroupClick(group: GroupResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): GroupsSimpleViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_text_black, parent, false)
            return GroupsSimpleViewHolder(view, listener)
        }
    }
}
