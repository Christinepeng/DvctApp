package com.divercity.android.features.onboarding.selectskill.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.data.entity.skills.SkillResponse
import kotlinx.android.synthetic.main.item_text.view.*

class  SkillsOnboardingViewHolder
private constructor(itemView: View, private val listener: Listener?) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: SkillResponse?) {

        data?.let {
            itemView.apply {
                item_text.text = it.attributes?.name
                setOnClickListener {
                    listener?.onSkillSelect(data)
                }
            }
        }
    }

    interface Listener {

        fun onSkillSelect(skill: SkillResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): SkillsOnboardingViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_text, parent, false)
            return SkillsOnboardingViewHolder(
                view,
                listener
            )
        }
    }
}
