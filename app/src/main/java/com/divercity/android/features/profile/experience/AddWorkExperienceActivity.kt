package com.divercity.android.features.profile.experience

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

/**
 * Created by lucas on 05/11/2018.
 */
 
class AddWorkExperienceActivity : BaseActivity(){

    companion object {

        fun getCallingIntent(context: Context?) : Intent {
            return Intent(context, AddWorkExperienceActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = AddWorkExperienceFragment.newInstance()
}