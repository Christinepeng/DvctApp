package com.divercity.android.features.user.addeditworkexperience.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.model.WorkExperience
import kotlinx.android.synthetic.main.item_work_experience.view.*
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class WorkExperienceAdapter @Inject
constructor() : RecyclerView.Adapter<WorkExperienceAdapter.Holder>() {

    var list: List<WorkExperience> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onWorkExperienceSelect: ((WorkExperience) -> Unit)? = null
    var onWorkExperienceDelete: ((WorkExperience) -> Unit)? = null

    var isEdition = false

    inner class Holder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(data: WorkExperience) =
            itemView.apply {

                GlideApp.with(this)
                    .load(data.companyPic)
                    .into(item_img)

                item_txt_title.text = data.role
                item_txt_subtitle1.text = data.companyName

                item_txt_subtitle2.text =
                    data.startDate.plus(" - " + if (data.isPresent!!) "Present" else data.endDate)

                if(isEdition) {
                    btn_delete.visibility = View.VISIBLE
                    btn_delete.setOnClickListener {
                        onWorkExperienceDelete?.invoke(data)
                    }
                }else {
                    btn_delete.visibility = View.GONE
                    btn_delete.setOnClickListener(null)
                }

                setOnClickListener {
                    onWorkExperienceSelect?.invoke(data)
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
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}