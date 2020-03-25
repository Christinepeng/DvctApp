package com.divercity.android.features.groups.adapter

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
import kotlinx.android.synthetic.main.item_group.view.item_group_btn_join_member
import kotlinx.android.synthetic.main.item_group.view.item_group_img
import kotlinx.android.synthetic.main.item_group.view.item_group_txt_members
import kotlinx.android.synthetic.main.item_group.view.item_group_txt_title
import kotlinx.android.synthetic.main.item_group_onboarding.view.*

class GroupsForOnboardingViewHolder
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

                checkbox_group_btn_join_member_onboarding.setOnClickListener {
                    if (checkbox_group_btn_join_member_onboarding.isChecked) {
                        checkboxSelectedAction(item_group_btn_join_member_onboarding, checkbox_group_btn_join_member_onboarding)
                        listener?.onGroupJoinClick(
                            GroupPosition(
                                position,
                                data
                            )
                        )
                    } else {
                        checkboxUnselectedAction(item_group_btn_join_member_onboarding, checkbox_group_btn_join_member_onboarding)
                        listener?.onGroupLeaveClick(
                            GroupPosition(
                                position,
                                data
                            )
                        )
                    }
                }

                if (data.attributes.isCurrentUserAdmin
                    || data.attributes.isFollowedByCurrent
                    || data.isJoinRequestPending()
                ) {
                    checkboxSelectedAction(item_group_btn_join_member_onboarding, checkbox_group_btn_join_member_onboarding)
                } else {
                    checkboxUnselectedAction(item_group_btn_join_member_onboarding, checkbox_group_btn_join_member_onboarding)
                }
            }
        }
    }

    fun checkboxSelectedAction(item_group_btn_join_member_onboarding: TextView, checkbox_group_btn_join_member: CheckBox) {
        item_group_btn_join_member_onboarding.setTextColor(ContextCompat.getColor(itemView.context, R.color.appBlue))
        checkbox_group_btn_join_member.setChecked(true)
        checkbox_group_btn_join_member.setButtonTintList(getColorStateList(itemView.context, R.color.appBlue))
    }

    fun checkboxUnselectedAction(item_group_btn_join_member_onboarding: TextView, checkbox_group_btn_join_member: CheckBox) {
        item_group_btn_join_member_onboarding.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey3))
        checkbox_group_btn_join_member.setChecked(false)
        checkbox_group_btn_join_member.setButtonTintList(getColorStateList(itemView.context, R.color.grey3))
    }

    interface Listener {

        fun onGroupJoinClick(groupPosition: GroupPosition)

        fun onGroupRequestJoinClick(groupPosition: GroupPosition)

        fun onGroupLeaveClick(groupPosition: GroupPosition)

        fun onGroupClick(position: Int, group: GroupResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): GroupsForOnboardingViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_group_onboarding, parent, false)
            return GroupsForOnboardingViewHolder(view, listener)
        }
    }
}
