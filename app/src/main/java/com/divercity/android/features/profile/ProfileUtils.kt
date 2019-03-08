package com.divercity.android.features.profile

import android.content.Context
import android.view.View
import com.divercity.android.R
import kotlinx.android.synthetic.main.view_user_personal_details.view.*

/**
 * Created by lucas on 07/03/2019.
 */

object ProfileUtils {

    fun setupProfileLayout(userType: String?,
                           context: Context,
                           personalView: View,
                           skillsView : View,
                           experienceView : View,
                           educationView : View,
                           interestsView : View){

        when (userType) {
            context.getString(R.string.professional_id),
            context.getString(R.string.job_seeker_id) -> {
                skillsView.visibility = View.VISIBLE
                experienceView.visibility = View.VISIBLE
                educationView.visibility = View.VISIBLE
                interestsView.visibility = View.VISIBLE

                personalView.lay_school.visibility = View.GONE
            }
            context.getString(R.string.recruiter_id),
            context.getString(R.string.hiring_manager_id) -> {
                skillsView.visibility = View.GONE
                experienceView.visibility = View.GONE
                educationView.visibility = View.GONE
                interestsView.visibility = View.GONE

                personalView.lay_resume.visibility = View.GONE
                personalView.lay_school.visibility = View.GONE
                personalView.lay_age_range.visibility = View.GONE
            }
            context.getString(R.string.student_id) -> {
                skillsView.visibility = View.VISIBLE
                experienceView.visibility = View.VISIBLE
                educationView.visibility = View.VISIBLE
                interestsView.visibility = View.VISIBLE
            }
            context.getString(R.string.entrepreneur_id) ->{
                skillsView.visibility = View.GONE
                experienceView.visibility = View.GONE
                educationView.visibility = View.GONE
                interestsView.visibility = View.VISIBLE
            }
        }
    }

    fun setupPersonalLayout(userType: String?, context: Context, personalView: View) {
        when (userType) {
            context.getString(R.string.professional_id),
            context.getString(R.string.job_seeker_id) -> {
                personalView.lay_school.visibility = View.GONE
            }
            context.getString(R.string.recruiter_id),
            context.getString(R.string.hiring_manager_id) -> {
                personalView.lay_resume.visibility = View.GONE
                personalView.lay_school.visibility = View.GONE
                personalView.lay_age_range.visibility = View.GONE
            }
        }
    }
}