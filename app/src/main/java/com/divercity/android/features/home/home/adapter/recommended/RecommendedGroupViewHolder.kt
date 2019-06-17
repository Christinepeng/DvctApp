package com.divercity.android.features.home.home.adapter.recommended

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.model.position.GroupPosition
import kotlinx.android.synthetic.main.item_recommended.view.*

class RecommendedGroupViewHolder
private constructor(itemView: View, private val listener: Listener?) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(groupPos: GroupPosition) {
        groupPos.group.let {
            GlideApp.with(itemView)
                .load(it.attributes.pictureMain)
                .apply(RequestOptions().circleCrop())
                .into(itemView.img)

            itemView.txt_title.text = it.attributes.title
            itemView.txt_subtitle1.text = it.attributes.followersCount.toString().plus(" Members")

            itemView.btn_action.apply {
                text = if (it.attributes.isFollowedByCurrent) {
                    setOnClickListener(null)
                    setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
                    setTextColor(ContextCompat.getColor(itemView.context, R.color.appBlue))
                    "Member"
                } else if (it.isJoinRequestPending()) {
                    setOnClickListener(null)
                    setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
                    setTextColor(ContextCompat.getColor(itemView.context, R.color.appBlue))
                    "Pending"
                } else {
                    if (it.isPublic())
                        setOnClickListener {
                            listener?.onGroupJoinClick(groupPos)
                        }
                    else
                        setOnClickListener {
                            listener?.onGroupRequestJoinClick(groupPos)
                        }
                    setBackgroundResource(R.drawable.shape_backgrd_round_blue3)
                    setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                    "Join"
                }
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            }

            itemView.setOnClickListener {
                listener?.onGroupClick(groupPos)
            }

            itemView.btn_close.setOnClickListener {
                listener?.onGroupDiscarded(groupPos)
            }
        }
    }

    interface Listener {
        fun onGroupRequestJoinClick(groupPos: GroupPosition)

        fun onGroupJoinClick(groupPos: GroupPosition)

        fun onGroupClick(groupPos: GroupPosition)

        fun onGroupDiscarded(groupPos: GroupPosition)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): RecommendedGroupViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_recommended, parent, false)
            return RecommendedGroupViewHolder(view, listener)
        }
    }
}
