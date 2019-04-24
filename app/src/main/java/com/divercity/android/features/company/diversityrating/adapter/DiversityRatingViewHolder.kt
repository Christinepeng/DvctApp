package com.divercity.android.features.company.diversityrating.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.utils.Util
import com.divercity.android.data.entity.company.review.CompanyDiversityReviewResponse
import com.divercity.android.data.entity.group.group.GroupResponse
import kotlinx.android.synthetic.main.item_review.view.*

class DiversityRatingViewHolder
private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: CompanyDiversityReviewResponse?) {
        data?.attributes?.let {
            itemView.apply {
                txt_date_review.text = Util.getStringDateWithServerDate(it.createdAt)
                txt_rating.text = it.rate.toString()
                txt_review.text = it.review
            }
        }
    }

    interface Listener {

        fun onGroupClick(group: GroupResponse)
    }

    companion object {

        fun create(parent: ViewGroup): DiversityRatingViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_review, parent, false)
            return DiversityRatingViewHolder(view)
        }
    }
}
