package com.divercity.android.features.profile.experience.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.workexperience.response.WorkExperienceResponse
import kotlinx.android.synthetic.main.item_work_experience.view.*
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class WorkExperienceAdapter @Inject
constructor() : RecyclerView.Adapter<WorkExperienceAdapter.Holder>() {

    var list: List<WorkExperienceResponse> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: ((WorkExperienceResponse) -> Unit)? = null

    class Holder(itemView: View, val listener: ((WorkExperienceResponse) -> Unit)?) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(data: WorkExperienceResponse) =
            itemView.apply {

                GlideApp.with(this)
                    .load(data.attributes?.jobEmployerInfo?.pictureMain)
                    .into(item_img)

                item_txt_title.text = data.attributes?.role
                item_txt_subtitle1.text = data.attributes?.jobEmployerInfo?.name

                item_txt_subtitle2.text =
                    data.attributes?.jobStart?.plus(" - " + if (data.attributes.isPresent!!) "Present" else data.attributes.jobEnd)

                setOnClickListener {
                    listener?.invoke(data)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_work_experience,
                parent,
                false
            )
        return Holder(view, listener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}