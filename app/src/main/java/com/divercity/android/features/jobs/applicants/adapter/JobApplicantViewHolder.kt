package com.divercity.android.features.jobs.applicants.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.jobapplication.JobApplicationResponse
import kotlinx.android.synthetic.main.item_user.view.*
import kotlinx.android.synthetic.main.item_user_mention.view.*

class JobApplicantViewHolder
private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: JobApplicationResponse?) {
        data?.let {
            itemView.apply {

                GlideApp.with(this)
                        .load(it.attributes?.applicant?.photos?.original)
                        .apply(RequestOptions().circleCrop())
                        .into(include_img_desc.img)

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
