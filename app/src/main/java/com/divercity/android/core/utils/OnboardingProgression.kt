package com.divercity.android.core.utils

import android.app.Activity
import com.divercity.android.R

/**
 * Created by lucas on 26/10/2018.
 */

object OnboardingProgression {

    private var COUNT_RECRUITER = Math.ceil(100 / 6.0).toInt()
    private var COUNT_HR = Math.ceil(100 / 7.0).toInt()
    private var COUNT_PROF_ENTREPNR_JOBSK = Math.ceil(100 / 10.0).toInt()
    private var COUNT_STUDENT = Math.ceil(100 / 9.0).toInt()

    fun getNextNavigationProgressOnboarding(
        activity: Activity,
        userTypeId: String,
        progress: Int
    ): Int {
        return when (userTypeId) {
            activity.getString(R.string.recruiter_id) -> {
                progress + COUNT_RECRUITER
            }
            activity.getString(R.string.hiring_manager_id) -> {
                progress + COUNT_HR
            }
            activity.getString(R.string.student_id) -> {
                progress + COUNT_STUDENT
            }
            else -> {
                progress + COUNT_PROF_ENTREPNR_JOBSK
            }
        }
    }
}