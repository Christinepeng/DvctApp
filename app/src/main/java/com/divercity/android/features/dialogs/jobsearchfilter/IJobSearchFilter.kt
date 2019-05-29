package com.divercity.android.features.dialogs.jobsearchfilter

import androidx.fragment.app.Fragment

/**
 * Created by lucas on 2019-05-28.
 */

interface IJobSearchFilter {

    fun replaceFragment(fragment : Fragment?, tag : String?)

    fun onBackPressed()
}