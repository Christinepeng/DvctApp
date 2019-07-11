package com.divercity.android.features.company.diversityrating.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.utils.Util
import com.divercity.android.model.CompanyDiversityReview
import kotlinx.android.synthetic.main.view_review.view.*
import kotlinx.android.synthetic.main.view_review_back.view.*
import kotlinx.android.synthetic.main.view_review_front.view.*

class DiversityRatingViewHolder
private constructor(itemView: View, val listener: Listener) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, isFlipped: Boolean, data: CompanyDiversityReview?) {
        data?.let {
            itemView.apply {

                if (isFlipped && flip_view.isFrontSide || !isFlipped && flip_view.isBackSide) {
                    flip_view.flipDuration = 0
                    flip_view.flipTheView()
                }

                txt_review.text =
                    if (it.review.isNullOrEmpty()) "No written review given" else it.review
                txt_date_front.text = Util.getStringDateWithServerDate(it.createdAt)
                rating_bar_overall.rating = it.rate?.toFloat() ?: 0.0f

                txt_date_back.text = Util.getStringDateWithServerDate(it.createdAt)

                setRatingValue(rating_bar_gender, data.genderRate)
                setRatingValue(rating_bar_race, data.raceEthnicityRate)
                setRatingValue(rating_bar_age, data.ageRate)
                setRatingValue(rating_bar_sex_orien, data.sexualOrientationRate)
                setRatingValue(rating_bar_abel_bodiedness, data.ableBodiednessRate)

                btn_flip_front.setOnClickListener {
                    listener.onViewFlipped(true, position)
                    flip_view.flipDuration = 400
                    flip_view.flipTheView()
                }

                btn_flip_back.setOnClickListener {
                    listener.onViewFlipped(false, position)
                    flip_view.flipDuration = 400
                    flip_view.flipTheView()
                }

                scroll_text.setOnTouchListener { p0, _ ->
                    p0?.parent?.requestDisallowInterceptTouchEvent(true)
                    false
                }
            }
        }
    }

    private fun setRatingValue(ratingBar: RatingBar, value: Int?) {
        ratingBar.rating = value?.toFloat() ?: 0f
    }

    interface Listener {

        fun onViewFlipped(state: Boolean, position: Int)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: Listener
        ): DiversityRatingViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.view_review, parent, false)
            return DiversityRatingViewHolder(view, listener)
        }
    }
}
