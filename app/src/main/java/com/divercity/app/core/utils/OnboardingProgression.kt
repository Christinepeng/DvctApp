package com.divercity.app.core.utils

import android.app.Activity
import com.divercity.app.R

/**
 * Created by lucas on 26/10/2018.
 */

object OnboardingProgression {

    fun getNextNavigationProgressOnboarding(activity: Activity, userTypeId: String, progress: Int): Int {
        var nextProgress = progress
        if (userTypeId == activity.getString(R.string.recruiter_id) || userTypeId == activity.getString(R.string.hiring_manager_id))
            nextProgress += 20
        else if (userTypeId == activity.getString(R.string.student_id))
            nextProgress += 12
        else
            nextProgress += 13
        return nextProgress
    }
}