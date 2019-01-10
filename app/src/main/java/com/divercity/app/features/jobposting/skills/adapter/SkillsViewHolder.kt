package com.divercity.app.features.jobposting.skills.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.app.R
import com.divercity.app.data.entity.skills.SkillResponse
import kotlinx.android.synthetic.main.item_btn_text.view.*

class SkillsViewHolder
private constructor(itemView: View, private val listener: Listener) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: SkillResponse?) {

        data?.let {
            itemView.txt_title.text = data.attributes?.name
            itemView.btn_public_private.isSelected = data.isSelected
            itemView.btn_public_private.setOnClickListener {
                itemView.btn_public_private.isSelected = !itemView.btn_public_private.isSelected
                data.isSelected = itemView.btn_public_private.isSelected
                listener.onSkillSelectUnselect(data)
            }
        }
    }

    interface Listener {

        fun onSkillSelectUnselect(skill: SkillResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener): SkillsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_btn_text, parent, false)
            return SkillsViewHolder(view, listener)
        }
    }
}
