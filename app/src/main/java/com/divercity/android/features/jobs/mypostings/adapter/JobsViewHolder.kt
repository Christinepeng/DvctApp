package com.divercity.android.features.jobs.mypostings.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.job.response.JobResponse
import kotlinx.android.synthetic.main.item_job.view.*

class JobsViewHolder
private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: JobResponse?) {
        data?.let {
            GlideApp.with(itemView)
                    .load(it.attributes?.employer?.photos?.original)
                    .into(itemView.item_jobs_img)

            itemView.item_jobs_txt_company.text = it.attributes?.employer?.name
            itemView.item_jobs_txt_place.text = it.attributes?.locationDisplayName
            itemView.item_jobs_txt_title.text = it.attributes?.title

            it.attributes?.publishable?.also { isPublished ->
                if(!isPublished) {
                    itemView.btn_job_action.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.btn_publish))
                    itemView.btn_job_action.setOnClickListener {
                        listener?.onPublishClick(position, data)
                    }
                }
                else {
                    itemView.btn_job_action.setImageDrawable(null)
                    itemView.btn_job_action.setOnClickListener(null)
                }
            }

            GlideApp.with(itemView)
                    .load(it.attributes?.recruiter?.avatarThumb)
                    .apply(RequestOptions().circleCrop())
                    .into(itemView.img_recruiter)

            itemView.txt_recruiter_name.text = it.attributes?.recruiter?.name
            itemView.txt_recruiter_occup.text = it.attributes?.recruiter?.occupation

            itemView.setOnClickListener {
                listener?.onJobClick(data)
            }
        }
    }

    interface Listener {

        fun onPublishClick(position: Int, job: JobResponse)

        fun onJobClick(job: JobResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): JobsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_job, parent, false)
            return JobsViewHolder(view, listener)
        }
    }
}
