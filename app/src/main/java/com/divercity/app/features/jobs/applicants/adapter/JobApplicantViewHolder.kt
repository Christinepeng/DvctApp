package com.divercity.app.features.jobs.applicants.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.divercity.app.R
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.data.entity.jobapplication.JobApplicationResponse
import kotlinx.android.synthetic.main.item_user.view.*

class JobApplicantViewHolder
private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: JobApplicationResponse?) {
        data?.let {
            itemView.apply {

                GlideApp.with(this)
                        .load(it.attributes?.applicant?.photos?.thumb)
                        .apply(RequestOptions().circleCrop())
                        .into(img)

                txt_name.text = it.attributes?.applicant?.name
            }
        }
    }

    interface Listener

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): JobApplicantViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user, parent, false)
            return JobApplicantViewHolder(view, listener)
        }
    }
}
