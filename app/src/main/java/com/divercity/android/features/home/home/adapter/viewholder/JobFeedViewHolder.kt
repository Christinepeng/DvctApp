package com.divercity.android.features.home.home.adapter.viewholder

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.Util
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.jobs.jobs.adapter.JobsViewHolder
import kotlinx.android.synthetic.main.item_feed_job.view.*

class JobFeedViewHolder
private constructor(itemView: View, private val listener: JobsViewHolder.Listener?, private val isLoggedUserJobSeeker : Boolean) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(position : Int, data: JobResponse?) {

        data?.let {
            itemView.apply {
                GlideApp.with(itemView)
                    .load(it.attributes?.employer?.photos?.thumb)
                    .into(item_jobs_img)

                item_jobs_txt_company.text = it.attributes?.employer?.name
                item_jobs_txt_place.text = it.attributes?.locationDisplayName
                item_jobs_txt_title.text = it.attributes?.title

                if(isLoggedUserJobSeeker) {
                    btn_job_action.visibility = View.VISIBLE
                    it.attributes?.isAppliedByCurrent?.also { isApplied ->
                        if (!isApplied) {
                            btn_job_action.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.btn_apply))
                            btn_job_action.setOnClickListener {
                                listener?.onApplyClick(position, data)
                            }
                        } else {
                            btn_job_action.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.btn_applied))
                            btn_job_action.setOnClickListener(null)
                        }
                    }
                } else {
                    btn_job_action.visibility = View.GONE
                }

                txt_job_poster_name.text = it.attributes?.recruiter?.name
                txt_job_created.text = " · ".plus(Util.getTimeAgoWithStringServerDate(it.attributes?.publishedOn))

                GlideApp.with(itemView)
                    .load(it.attributes?.recruiter?.avatarThumb)
                    .apply(RequestOptions().circleCrop())
                    .into(img_job_poster)

                setOnClickListener {
                    listener?.onJobClick(data)
                }
            }
        }
    }

    companion object {

        fun create(parent: ViewGroup, listener: JobsViewHolder.Listener?, isLoggedUserJobSeeker : Boolean): JobFeedViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_feed_job, parent, false)
            return JobFeedViewHolder(view, listener, isLoggedUserJobSeeker)
        }
    }
}
