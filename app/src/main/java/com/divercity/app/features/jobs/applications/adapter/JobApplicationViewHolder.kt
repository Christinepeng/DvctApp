package com.divercity.app.features.jobs.applications.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.app.R
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.data.entity.jobapplication.JobApplicationResponse
import kotlinx.android.synthetic.main.item_job.view.*

class JobApplicationViewHolder
private constructor(itemView: View, private val listener: Listener?, private val isLoggedUserJobSeeker: Boolean) :
        RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: JobApplicationResponse?) {
        data?.let {
            GlideApp.with(itemView)
                    .load(it.attributes?.employer?.photos?.thumb)
                    .into(itemView.item_jobs_img)

            itemView.item_jobs_txt_company.text = it.attributes?.employer?.name
            itemView.item_jobs_txt_place.text =
                    it.attributes?.employer?.address.plus(", ").plus(it.attributes?.employer?.country)
            itemView.item_jobs_txt_title.text = it.attributes?.jobTitle

            itemView.btn_job_action.setImageDrawable(
                    ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.btn_applied
                    )
            )
        }
    }

    interface Listener {

//        fun onApplyClick(position: Int, job: JobApplicationResponse)
//
//        fun onJobClick(job: JobApplicationResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?, isLoggedUserJobSeeker: Boolean): JobApplicationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_job, parent, false)
            return JobApplicationViewHolder(view, listener, isLoggedUserJobSeeker)
        }
    }
}
