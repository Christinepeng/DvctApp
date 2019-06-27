package com.divercity.android.features.home.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.Util
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.model.position.JobPosition
import kotlinx.android.synthetic.main.item_feed_job.view.*

class JobFeedViewHolder
private constructor(
    itemView: View,
    private val listener: Listener?,
    private val isLoggedUserJobSeeker: Boolean
) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: JobResponse?) {

        data?.let {
            itemView.apply {
                GlideApp.with(itemView)
                    .load(it.attributes?.employer?.photos?.original)
                    .into(item_jobs_img)

                item_jobs_txt_company.text = it.attributes?.employer?.name
                item_jobs_txt_place.text = it.attributes?.locationDisplayName
                item_jobs_txt_title.text = it.attributes?.title

                if (isLoggedUserJobSeeker) {
                    btn_apply.visibility = View.VISIBLE
                    it.attributes?.isAppliedByCurrent?.also { isApplied ->
                        if (!isApplied) {
                            btn_apply.background = ContextCompat.getDrawable(
                                context!!,
                                R.drawable.shape_backgrd_round_blue3
                            )
                            btn_apply.setTextColor(ContextCompat.getColor(context!!, R.color.white))
                            btn_apply.setText(R.string.apply)
                            btn_apply.setOnClickListener {
                                listener?.onApplyClick(JobPosition(position, data))
                            }
                        } else {
                            btn_apply.background = ContextCompat.getDrawable(
                                context!!,
                                R.drawable.bk_white_stroke_blue_rounded
                            )
                            btn_apply.setTextColor(
                                ContextCompat.getColor(
                                    context!!,
                                    R.color.appBlue
                                )
                            )
                            btn_apply.setText(R.string.applied)
                        }
                    }
                } else {
                    btn_apply.visibility = View.GONE
                }

                it.attributes?.isBookmarkedByCurrent?.let {
                    if (it) {
                        btn_save.background = ContextCompat.getDrawable(
                            context!!,
                            R.drawable.shape_backgrd_round_blue3
                        )
                        btn_save.setTextColor(ContextCompat.getColor(context!!, R.color.white))
                        btn_save.setText(R.string.saved)
                        btn_save.setOnClickListener {
                            listener?.onSaveUnsaveClick(false, JobPosition(position, data))
                        }
                    } else {
                        btn_save.background = ContextCompat.getDrawable(
                            context!!,
                            R.drawable.bk_white_stroke_blue_rounded
                        )
                        btn_save.setTextColor(ContextCompat.getColor(context!!, R.color.appBlue))
                        btn_save.setText(R.string.save)
                        btn_save.setOnClickListener {
                            listener?.onSaveUnsaveClick(true, JobPosition(position, data))
                        }
                    }
                }

                item_jobs_txt_poster_name.text = it.attributes?.recruiter?.name
                item_jobs_txt_poster_occupation.text = it.attributes?.recruiter?.occupation

                txt_job_created.text =
                    Util.getTimeAgoWithStringServerDate(it.attributes?.publishedOn)

                GlideApp.with(itemView)
                    .load(it.attributes?.recruiter?.avatarThumb)
                    .apply(RequestOptions().circleCrop())
                    .into(img_job_poster)

                setOnClickListener {
                    listener?.onJobClick(JobPosition(position, data))
                }
            }
        }
    }

    interface Listener {

        fun onApplyClick(jobPos: JobPosition)

        fun onJobClick(jobPos: JobPosition)

        fun onSaveUnsaveClick(save: Boolean, jobPos: JobPosition)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: Listener?,
            isLoggedUserJobSeeker: Boolean
        ): JobFeedViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_feed_job, parent, false)
            return JobFeedViewHolder(view, listener, isLoggedUserJobSeeker)
        }
    }
}
