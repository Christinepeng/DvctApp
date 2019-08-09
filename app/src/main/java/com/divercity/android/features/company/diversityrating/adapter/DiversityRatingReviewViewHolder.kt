package com.divercity.android.features.company.diversityrating.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.utils.Util
import com.divercity.android.model.CompanyDiversityReview
import kotlinx.android.synthetic.main.item_review.view.*
import kotlinx.android.synthetic.main.view_rating.view.*

class DiversityRatingReviewViewHolder
private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: CompanyDiversityReview?) {
        data?.let {
            itemView.apply {

                btn_see_more_less.visibility = View.GONE

                setExpandReviewState(data.isReviewExpanded)
                btn_see_more_less.setOnClickListener {
                    data.isReviewExpanded = !data.isReviewExpanded
                    setExpandReviewState(data.isReviewExpanded)
                }

                txt_review.text =
                    if (it.review.isNullOrEmpty()) "No written review given" else it.review

                txt_review.post {
                    val lineCount = txt_review.lineCount
                    if (lineCount > MAX_LINES)
                        btn_see_more_less.visibility = View.VISIBLE
                    else
                        btn_see_more_less.visibility = View.GONE
                }

                setExpandRatingState(data.isRatingExpanded)
                btn_tap_for_more_less.setOnClickListener {
                    data.isRatingExpanded = !data.isRatingExpanded
                    setExpandRatingState(data.isRatingExpanded)
                }

                txt_date_review.text = Util.getStringDateWithServerDate(it.createdAt)
                rating_bar_overall.rating = it.rate?.toFloat() ?: 0.0f

                lay_gender.lbl_rating.setText(R.string.gender)
                lay_age.lbl_rating.setText(R.string.age)
                lay_bodness.lbl_rating.setText(R.string.able_bodiedness)
                lay_race.lbl_rating.setText(R.string.race_ethnicity)
                lay_sex_orien.lbl_rating.setText(R.string.sexual_orientation)

                setRatingValue(lay_gender.rating_bar, data.genderRate)
                setRatingValue(lay_age.rating_bar, data.raceEthnicityRate)
                setRatingValue(lay_bodness.rating_bar, data.ageRate)
                setRatingValue(lay_race.rating_bar, data.sexualOrientationRate)
                setRatingValue(lay_sex_orien.rating_bar, data.ableBodiednessRate)
            }
        }
    }

    private fun setRatingValue(ratingBar: RatingBar, value: Int?) {
        ratingBar.rating = value?.toFloat() ?: 0f
    }

    private fun setExpandReviewState(isReviewExpanded: Boolean) {
        itemView.apply {
            if (isReviewExpanded) {
                btn_see_more_less.setText(R.string.see_less)
                txt_review.maxLines = Int.MAX_VALUE
            } else {
                btn_see_more_less.setText(R.string.see_more)
                txt_review.maxLines = MAX_LINES
            }
        }
    }

    private fun setExpandRatingState(isRatingExpanded: Boolean) {
        itemView.apply {
            if (isRatingExpanded) {
                btn_tap_for_more_less.setText(R.string.tap_for_less)
                lay_all_ratings.visibility = View.VISIBLE
            } else {
                btn_tap_for_more_less.setText(R.string.tap_for_more)
                lay_all_ratings.visibility = View.GONE
            }
        }
    }

    companion object {

        private const val MAX_LINES = 9

        fun create(
            parent: ViewGroup
        ): DiversityRatingReviewViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_review, parent, false)
            return DiversityRatingReviewViewHolder(view)
        }
    }
}
