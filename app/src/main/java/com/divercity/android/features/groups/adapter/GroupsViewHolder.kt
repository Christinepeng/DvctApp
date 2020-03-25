package com.divercity.android.features.groups.adapter

import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
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




//                checkbox_group_btn_join_member.setOnClickListener {
//                    if (checkbox_group_btn_join_member.isChecked) {
//                        checkboxSelectedAction(item_group_btn_join_member, checkbox_group_btn_join_member)
//                        listener?.onGroupJoinClick(
//                            GroupPosition(
//                                position,
//                                data
//                            )
//                        )
//                    } else {
//                        checkboxUnselectedAction(item_group_btn_join_member, checkbox_group_btn_join_member)
//                        listener?.onGroupLeaveClick(
//                            GroupPosition(
//                                position,
//                                data
//                            )
//                        )
//                    }
//                }
//
//                if (data.attributes.isCurrentUserAdmin
//                    || data.attributes.isFollowedByCurrent
//                    || data.isJoinRequestPending()
//                ) {
//                    checkboxSelectedAction(item_group_btn_join_member, checkbox_group_btn_join_member)
//                } else {
//                    checkboxUnselectedAction(item_group_btn_join_member, checkbox_group_btn_join_member)
//                }





                item_group_btn_join_member.apply {
                    isEnabled = true
                    text = if (data.attributes.isCurrentUserAdmin) {
                        setOnClickListener(null)
                        setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
                        setTextColor(ContextCompat.getColor(itemView.context, R.color.appBlue))
//                        item_group_btn_join_member.setBackgroundColor(Color.rgb(64, 131, 207))
//                        "Admin" "Joined"
                        "Joined"
                    } else if (data.attributes.isFollowedByCurrent) {
                        setOnClickListener(null)
                        setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
                        setTextColor(ContextCompat.getColor(itemView.context, R.color.appBlue))
//                        item_group_btn_join_member.setBackgroundColor(Color.rgb(64, 131, 207))
                        "Joined"
//                        "Member" "Joined"
                    } else if (data.isJoinRequestPending()) {
                        setOnClickListener(null)
                        setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
                        setTextColor(ContextCompat.getColor(itemView.context, R.color.appBlue))
//                        item_group_btn_join_member.setBackgroundColor(Color.rgb(64, 131, 207))
                        "Request Sent"
//                        "Pending" "Request Sent"

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
//                        setBackgroundResource(R.drawable.bk_white_stroke_white_rounded)
                        setTextColor(
//                            Color.parseColor("#a4a4b3")
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

//    fun checkboxSelectedAction(item_group_btn_join_member: TextView, checkbox_group_btn_join_member: CheckBox) {
//        item_group_btn_join_member.setTextColor(ContextCompat.getColor(itemView.context, R.color.appBlue))
//        checkbox_group_btn_join_member.setChecked(true)
//        checkbox_group_btn_join_member.setButtonTintList(getColorStateList(itemView.context, R.color.appBlue))
//    }
//
//    fun checkboxUnselectedAction(item_group_btn_join_member: TextView, checkbox_group_btn_join_member: CheckBox) {
//        item_group_btn_join_member.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey3))
//        checkbox_group_btn_join_member.setChecked(false)
//        checkbox_group_btn_join_member.setButtonTintList(getColorStateList(itemView.context, R.color.grey3))
//    }

    interface Listener {

        fun onGroupJoinClick(groupPosition: GroupPosition)

        fun onGroupRequestJoinClick(groupPosition: GroupPosition)

        fun onGroupLeaveClick(groupPosition: GroupPosition)

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
