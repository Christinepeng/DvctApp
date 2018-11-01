package com.divercity.app.features.home.jobs.jobs.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.divercity.app.R
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.data.entity.job.JobResponse
import kotlinx.android.synthetic.main.item_jobs.view.*

class JobsViewHolder
private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: JobResponse?) {
        data?.let {
            GlideApp.with(itemView.context)
                    .load(it.attributes?.employer?.photos?.thumb)
                    .into(itemView.item_jobs_img)

            itemView.item_jobs_txt_company.text = it.attributes?.employer?.name
            itemView.item_jobs_txt_place.text = "Argentina"
            itemView.item_jobs_txt_title.text = it.attributes?.title
        }
    }

    interface Listener {

        fun onSaveClick(position: Int, job: JobResponse)

        fun onJobClick(job: JobResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): JobsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_jobs, parent, false)
            return JobsViewHolder(view, listener)
        }
    }
}
