package com.divercity.app.features.groups.adapter

import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.divercity.app.R
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.data.entity.group.GroupResponse
import kotlinx.android.synthetic.main.item_group.view.*

class GroupsViewHolder
private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: GroupResponse?) {
        data?.let {
            try {
                itemView.item_group_img.visibility = View.VISIBLE
                val urlMain = data.attributes.pictureMain
                GlideApp.with(itemView.context)
                        .load(urlMain)
                        .apply(RequestOptions().transforms(RoundedCorners(16)))
                        .into(itemView.item_group_img)
            } catch (e: NullPointerException) {
                itemView.item_group_img.visibility = View.GONE
            }

            itemView.item_group_txt_title.setText(data.attributes.title)
            itemView.item_group_txt_members.setText(data.attributes.followersCount.toString() + " Members")

            if (data.attributes.isIsFollowedByCurrent) {
                itemView.item_group_btn_join_member.setOnClickListener(null)
                itemView.item_group_btn_join_member.setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
                itemView.item_group_btn_join_member.setTextColor(itemView.context.resources.getColor(R.color.appBlue))
                itemView.item_group_btn_join_member.setText("Member")
            } else {
                itemView.item_group_btn_join_member.setOnClickListener { listener?.onGroupJoinClick(position, data) }
                itemView.item_group_btn_join_member.setBackgroundResource(R.drawable.shape_backgrd_round_blue3)
                itemView.item_group_btn_join_member.setTextColor(itemView.context.resources.getColor(android.R.color.white))
                itemView.item_group_btn_join_member.setText("Join")
            }
            itemView.item_group_btn_join_member.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        }
    }

    interface Listener {

        fun onGroupJoinClick(position: Int, group: GroupResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): GroupsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_group, parent, false)
            return GroupsViewHolder(view, listener)
        }
    }
}
