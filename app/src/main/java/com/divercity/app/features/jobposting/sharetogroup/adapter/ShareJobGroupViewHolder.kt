package com.divercity.app.features.jobposting.sharetogroup.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.app.R
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.data.entity.group.GroupResponse
import kotlinx.android.synthetic.main.item_group_share.view.*

class ShareJobGroupViewHolder private constructor(itemView: View, private val listener: Listener) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: GroupResponse) {
        try {
            itemView.item_group_img.visibility = View.VISIBLE
            val urlMain = data.attributes.pictureMain
            GlideApp.with(itemView.context)
                    .load(urlMain).into(itemView.item_group_img)
        } catch (e: NullPointerException) {
            itemView.item_group_img.visibility = View.GONE
        }

        itemView.item_group_txt_title.text = data.attributes.title
        itemView.item_group_txt_members.text = data.attributes.followersCount.toString().plus(" Members")

        itemView.btn_select_unselect.setOnClickListener {
            itemView.btn_select_unselect.isSelected = !itemView.btn_select_unselect.isSelected
            listener.onGroupShareClick(data, itemView.btn_select_unselect.isSelected)
        }

    }

    interface Listener {
        fun onGroupShareClick(group: GroupResponse, isSelected : Boolean)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener): ShareJobGroupViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_group_share, parent, false)
            return ShareJobGroupViewHolder(view, listener)
        }
    }
}
