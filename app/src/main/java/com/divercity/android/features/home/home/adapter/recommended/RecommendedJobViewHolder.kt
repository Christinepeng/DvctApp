package com.divercity.android.features.home.home.adapter.recommended

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.model.position.JobPosition
import kotlinx.android.synthetic.main.item_recommended.view.*

class RecommendedJobViewHolder
private constructor(
    itemView: View,
    private val listener: Listener?,
    val isLoggedUserJobSeeker: Boolean
) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: JobResponse?) {
        data?.let {
            GlideApp.with(itemView)
                .load(it.attributes?.employer?.photos?.original)
                .apply(RequestOptions().circleCrop())
                .into(itemView.img)

            itemView.txt_title.text = it.attributes?.title
            itemView.txt_subtitle1.text =
                it.attributes?.employer?.name.plus("\n").plus(it.attributes?.locationDisplayName)

            if (isLoggedUserJobSeeker) {
                itemView.btn_action.visibility = View.VISIBLE
                itemView.btn_action.setText(R.string.apply)
                it.attributes?.isAppliedByCurrent?.let {
                    if (!it) {
                        itemView.btn_action.background = ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.shape_backgrd_round_blue3
                        )
                        itemView.btn_action.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.white
                            )
                        )
                        itemView.btn_action.setText(R.string.apply)
                        itemView.btn_action.setOnClickListener {
                            listener?.onApplyClick(JobPosition(position, data))
                        }
                    } else {
                        itemView.btn_action.background = ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.bk_white_stroke_blue_rounded
                        )
                        itemView.btn_action.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.appBlue
                            )
                        )
                        itemView.btn_action.setText(R.string.applied)
                    }
                }
            } else {
                itemView.btn_action.visibility = View.INVISIBLE
            }

            itemView.setOnClickListener {
                listener?.onJobClick(JobPosition(position, data))
            }

            itemView.btn_close.setOnClickListener {
                listener?.onJobDiscarded(JobPosition(position, data))
            }
        }
    }

    interface Listener {

        fun onApplyClick(jobPos: JobPosition)

        fun onJobClick(jobPos: JobPosition)

        fun onJobDiscarded(jobPos: JobPosition)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: Listener?,
            isLoggedUserJobSeeker: Boolean
        ): RecommendedJobViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_recommended, parent, false)
            return RecommendedJobViewHolder(view, listener, isLoggedUserJobSeeker)
        }
    }
}
