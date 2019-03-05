package com.divercity.android.features.jobs.savedjobs.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.android.R
import com.divercity.android.data.entity.job.response.JobResponse

class SavedJobsViewHolder
private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: JobResponse?) {
//        data?.let {
//            GlideApp.with(itemView)
//                    .load(it.userAttributes?.employer?.photos?.thumb)
//                    .into(itemView.item_jobs_img)
//
//            itemView.item_jobs_txt_company.text = it.userAttributes?.employer?.name
//            itemView.item_jobs_txt_place.text = it.userAttributes?.locationDisplayName
//            itemView.item_jobs_txt_title.text = it.userAttributes?.title
//            itemView.item_jobs_lay_btn_save.visibility = View.GONE
//            itemView.setOnClickListener {
//                listener?.onJobClick(data)
//            }
//        }
    }

    interface Listener {

        fun onJobClick(job: JobResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): SavedJobsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_job, parent, false)
            return SavedJobsViewHolder(view, listener)
        }
    }
}
