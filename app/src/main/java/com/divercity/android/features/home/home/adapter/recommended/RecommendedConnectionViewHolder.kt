package com.divercity.android.features.home.home.adapter.recommended

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.model.position.UserPosition
import com.divercity.android.model.user.User
import kotlinx.android.synthetic.main.item_recommended_connection.view.*

class RecommendedConnectionViewHolder
private constructor(itemView: View, private val listener: Listener?) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: User?) {
        data?.let {
            itemView.apply {

                GlideApp.with(this)
                    .load(it.avatarMedium)
                    .apply(RequestOptions().circleCrop())
                    .into(img.img)

                when (data.connected) {
                    "connected" -> {
                        btn_action.setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
                        btn_action.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.appBlue
                            )
                        )
                        btn_action.setText(R.string.connection)
                        btn_action.visibility = View.VISIBLE
                        btn_action.setOnClickListener(null)
                    }
                    "requested" -> {
                        btn_action.setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
                        btn_action.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.appBlue
                            )
                        )
                        btn_action.setText(R.string.requested)
                        btn_action.visibility = View.VISIBLE
                        btn_action.setOnClickListener(null)
                    }
                    "pending_approval" -> {
                        btn_action.setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
                        btn_action.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.appBlue
                            )
                        )
                        btn_action.setText(R.string.pending)
                        btn_action.visibility = View.VISIBLE
                        btn_action.setOnClickListener(null)
                    }
                    "not_connected" -> {
                        btn_action.setBackgroundResource(R.drawable.shape_backgrd_round_blue3)
                        btn_action.setTextColor(
                            ContextCompat.getColor(
                                context,
                                android.R.color.white
                            )
                        )
                        btn_action.setText(R.string.connect)
                        btn_action.visibility = View.VISIBLE
                        btn_action.setOnClickListener {
                            listener?.onConnectClick(UserPosition(position, data))
                        }
                    }
                }

                txt_title.text = it.name
                txt_subtitle1.text = it.occupation

                btn_close.setOnClickListener {
                    listener?.onUserDiscarded(UserPosition(position, data))
                }

                setOnClickListener {
                    listener?.onUserClick(UserPosition(position, data))
                }
            }
        }
    }

    interface Listener {

        fun onUserClick(userPosition: UserPosition)

        fun onConnectClick(userPosition: UserPosition)

        fun onUserDiscarded(userPosition: UserPosition)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): RecommendedConnectionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_recommended_connection, parent, false)
            return RecommendedConnectionViewHolder(view, listener)
        }
    }
}
