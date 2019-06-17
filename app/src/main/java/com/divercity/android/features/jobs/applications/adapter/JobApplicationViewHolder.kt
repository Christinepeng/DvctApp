package com.divercity.android.features.jobs.applications.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.jobapplication.JobApplicationResponse
import kotlinx.android.synthetic.main.item_job.view.*

class JobApplicationViewHolder
private constructor(itemView: View, private val listener: Listener?, private val isLoggedUserJobSeeker: Boolean) :
        RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: JobApplicationResponse?) {
        data?.let {
            GlideApp.with(itemView)
                    .load(it.attributes?.employer?.photos?.original)
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

//        fun onApplyClick(position: Int, companyName: JobApplicationResponse)
//
//        fun onJobClick(companyName: JobApplicationResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?, isLoggedUserJobSeeker: Boolean): JobApplicationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_job, parent, false)
            return JobApplicationViewHolder(view, listener, isLoggedUserJobSeeker)
        }
    }
}
