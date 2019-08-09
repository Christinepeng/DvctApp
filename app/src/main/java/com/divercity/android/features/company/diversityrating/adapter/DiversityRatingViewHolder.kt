package com.divercity.android.features.company.diversityrating.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.data.entity.company.response.CompanyResponse
import kotlinx.android.synthetic.main.view_all_ratings.view.*
import kotlinx.android.synthetic.main.view_rating.view.*

class DiversityRatingViewHolder
private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(company: CompanyResponse?) {
        itemView.apply {
            company?.attributes?.also {
                val diversityRating = it.divercityRating

                lay_gender.lbl_rating.setText(R.string.gender)
                lay_age.lbl_rating.setText(R.string.age)
                lay_bodness.lbl_rating.setText(R.string.able_bodiedness)
                lay_race.lbl_rating.setText(R.string.race_ethnicity)
                lay_sex_orien.lbl_rating.setText(R.string.sexual_orientation)

                lay_gender.txt_rating.text = diversityRating?.genderRate.toString()
                lay_age.txt_rating.text = diversityRating?.ageRate.toString()
                lay_race.txt_rating.text = diversityRating?.raceEthnicityRate.toString()
                lay_bodness.txt_rating.text = diversityRating?.ableBodiednessRate.toString()
                lay_sex_orien.txt_rating.text = diversityRating?.sexualOrientationRate.toString()

                lay_gender.rating_bar.rating = diversityRating?.genderRate?.toFloat() ?: 0f
                lay_age.rating_bar.rating = diversityRating?.ageRate?.toFloat() ?: 0f
                lay_race.rating_bar.rating = diversityRating?.raceEthnicityRate?.toFloat() ?: 0f
                lay_bodness.rating_bar.rating = diversityRating?.ableBodiednessRate?.toFloat() ?: 0f
                lay_sex_orien.rating_bar.rating =
                    diversityRating?.sexualOrientationRate?.toFloat() ?: 0f
            }
        }
    }

    companion object {

        fun create(
            parent: ViewGroup
        ): DiversityRatingViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.view_all_ratings, parent, false)
            return DiversityRatingViewHolder(view)
        }
    }
}
