package com.divercity.android.features.jobs.jobs.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.job.response.JobResponse
import kotlinx.android.synthetic.main.item_job.view.*

class JobsViewHolder
private constructor(itemView: View, private val listener: Listener?,private val isLoggedUserJobSeeker : Boolean) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: JobResponse?) {
        data?.let {
            GlideApp.with(itemView)
                    .load(it.attributes?.employer?.photos?.original)
                    .into(itemView.item_jobs_img)

            itemView.item_jobs_txt_company.text = it.attributes?.employer?.name
            itemView.item_jobs_txt_place.text = it.attributes?.locationDisplayName
            itemView.item_jobs_txt_title.text = it.attributes?.title

            if(isLoggedUserJobSeeker) {
                itemView.btn_job_action.visibility = View.VISIBLE
                it.attributes?.isAppliedByCurrent?.also { isApplied ->
                    if (!isApplied) {
                        itemView.btn_job_action.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.btn_apply))
                        itemView.btn_job_action.setOnClickListener {
                            listener?.onApplyClick(position, data)
                        }
                    } else {
                        itemView.btn_job_action.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.btn_applied))
                        itemView.btn_job_action.setOnClickListener(null)
                    }
                }
            } else {
                itemView.btn_job_action.visibility = View.GONE
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

        fun onApplyClick(position: Int, job: JobResponse)

        fun onJobClick(job: JobResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?, isLoggedUserJobSeeker : Boolean): JobsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_job, parent, false)
            return JobsViewHolder(view, listener, isLoggedUserJobSeeker)
        }
    }
}
