package com.divercity.android.features.home.home.recommended

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.group.GroupResponse
import kotlinx.android.synthetic.main.item_recommended.view.*

class RecommendedGroupViewHolder
private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: GroupResponse?) {
        data?.let {
            GlideApp.with(itemView)
                    .load(it.attributes?.pictureMain)
                    .apply(RequestOptions().circleCrop())
                    .into(itemView.img)

            itemView.txt_title.text = it.attributes?.title
            itemView.txt_subtitle.text = it.attributes?.followersCount.toString().plus(" Members")

            itemView.btn_action.apply {
                text = if (data.attributes.isIsFollowedByCurrent) {
                    setOnClickListener(null)
                    setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
                    setTextColor(ContextCompat.getColor(itemView.context, R.color.appBlue))
                    "Member"
                } else if (data.isJoinRequestPending) {
                    setOnClickListener(null)
                    setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
                    setTextColor(ContextCompat.getColor(itemView.context, R.color.appBlue))
                    "Pending"
                } else {
                    if (data.isPublic)
                        setOnClickListener { listener?.onGroupJoinClick(position, data) }
                    else
                        setOnClickListener { listener?.onGroupRequestJoinClick(position, data) }
                    setBackgroundResource(R.drawable.shape_backgrd_round_blue3)
                    setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                    "Join"
                }
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            }

            itemView.setOnClickListener {
                listener?.onGroupClick(data)
            }
        }
    }

    interface Listener {
        fun onGroupRequestJoinClick(position: Int, group: GroupResponse)

        fun onGroupJoinClick(position: Int, group: GroupResponse)

        fun onGroupClick(group: GroupResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): RecommendedGroupViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_recommended, parent, false)
            return RecommendedGroupViewHolder(view, listener)
        }
    }
}
