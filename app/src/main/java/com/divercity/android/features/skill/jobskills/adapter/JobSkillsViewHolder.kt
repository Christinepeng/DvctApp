package com.divercity.android.features.skill.jobskills.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.features.skill.jobskills.model.SkillSelectedModel
import kotlinx.android.synthetic.main.item_btn_text.view.*

class JobSkillsViewHolder
private constructor(itemView: View, private val listener: Listener) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: SkillSelectedModel?) {

        data?.let {
            itemView.txt_title.text = data.data.attributes?.name
            itemView.btn_select_unselect.isSelected = data.isSelected
            itemView.btn_select_unselect.setOnClickListener {
                itemView.btn_select_unselect.isSelected = !itemView.btn_select_unselect.isSelected
                data.isSelected = itemView.btn_select_unselect.isSelected
                listener.onSkillSelectUnselect(data)
            }
        }
    }

    interface Listener {

        fun onSkillSelectUnselect(skill: SkillSelectedModel)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener): JobSkillsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_btn_text, parent, false)
            return JobSkillsViewHolder(view, listener)
        }
    }
}
