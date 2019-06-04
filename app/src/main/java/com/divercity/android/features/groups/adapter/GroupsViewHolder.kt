package com.divercity.android.features.groups.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.model.position.GroupPosition
import kotlinx.android.synthetic.main.item_group.view.*

class GroupsViewHolder
private constructor(itemView: View, private val listener: Listener?) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: GroupResponse?) {
        data?.let {
            itemView.apply {
                GlideApp.with(this)
                    .load(data.attributes.pictureMain)
                    .transform(RoundedCorners(16))
                    .into(itemView.item_group_img)

                item_group_txt_title.text = data.attributes.title
                item_group_txt_members.text =
                    data.attributes.followersCount.toString().plus(" Members")

                item_group_btn_join_member.apply {
                    isEnabled = true
                    text = if (data.attributes.isCurrentUserAdmin) {
                        setOnClickListener(null)
                        setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
                        setTextColor(ContextCompat.getColor(itemView.context, R.color.appBlue))
                        "Admin"
                    } else if (data.attributes.isFollowedByCurrent) {
                        setOnClickListener(null)
                        setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
                        setTextColor(ContextCompat.getColor(itemView.context, R.color.appBlue))
                        "Member"
                    } else if (data.isJoinRequestPending()) {
                        setOnClickListener(null)
                        setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
                        setTextColor(ContextCompat.getColor(itemView.context, R.color.appBlue))
                        "Pending"
                    } else {
                        if (data.isPublic())
                            setOnClickListener {
                                isEnabled = false
                                listener?.onGroupJoinClick(
                                    GroupPosition(
                                        position,
                                        data
                                    )
                                )
                            }
                        else
                            setOnClickListener {
                                isEnabled = false
                                listener?.onGroupRequestJoinClick(
                                    GroupPosition(
                                        position,
                                        data
                                    )
                                )
                            }
                        setBackgroundResource(R.drawable.shape_backgrd_round_blue3)
                        setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                android.R.color.white
                            )
                        )
                        "Join"
                    }
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                }

                setOnClickListener {
                    listener?.onGroupClick(position, data)
                }
            }
        }
    }

    interface Listener {

        fun onGroupRequestJoinClick(groupPosition: GroupPosition)

        fun onGroupJoinClick(groupPosition: GroupPosition)

        fun onGroupClick(position: Int, group: GroupResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): GroupsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_group, parent, false)
            return GroupsViewHolder(view, listener)
        }
    }
}
