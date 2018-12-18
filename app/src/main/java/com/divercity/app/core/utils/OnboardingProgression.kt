package com.divercity.app.core.utils

import android.app.Activity
import com.divercity.app.R

/**
 * Created by lucas on 26/10/2018.
 */

object OnboardingProgression {

    private const val COUNT_RECRUITER_HR = 100 / 5
    private const val COUNT_STUDENT = (100 / 8) + 1
    private const val COUNT_PROF_ENTREPNR_JOBSK = (100 / 7) + 1

    fun getNextNavigationProgressOnboarding(activity: Activity, userTypeId: String, progress: Int): Int {

        return if (userTypeId == activity.getString(R.string.recruiter_id) ||
                userTypeId == activity.getString(R.string.hiring_manager_id))
            progress + COUNT_RECRUITER_HR
        else if (userTypeId == activity.getString(R.string.student_id))
            progress + COUNT_STUDENT
        else
            progress + COUNT_PROF_ENTREPNR_JOBSK
    }
}