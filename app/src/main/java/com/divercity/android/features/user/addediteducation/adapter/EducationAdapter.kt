package com.divercity.android.features.user.addediteducation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.model.Education
import kotlinx.android.synthetic.main.item_education.view.*
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class EducationAdapter @Inject
constructor() : RecyclerView.Adapter<EducationAdapter.Holder>() {

    var list: List<Education> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onEducationSelect: ((Education) -> Unit)? = null
    var onEducationDelete: ((Education) -> Unit)? = null

    var isEdition = false

    inner class Holder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(data: Education) =
            itemView.apply {

//                GlideApp.with(this)
//                    .load(data.attributes?.jobEmployerInfo?.pictureMain)
//                    .into(item_img)

                txt_first_letter.text = data.schoolName?.first().toString()
                item_txt_title.text = data.schoolName
                item_txt_subtitle1.text = data.major

                item_txt_subtitle2.text =
                    data.startYear.plus(" - ").plus(data.endYear)

                if(isEdition) {
                    btn_delete.visibility = View.VISIBLE
                    btn_delete.setOnClickListener {
                        onEducationDelete?.invoke(data)
                    }
                }else {
                    btn_delete.visibility = View.GONE
                    btn_delete.setOnClickListener(null)
                }

                setOnClickListener {
                    onEducationSelect?.invoke(data)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_education,
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