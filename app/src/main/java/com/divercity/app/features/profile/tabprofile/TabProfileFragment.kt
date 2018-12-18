package com.divercity.app.features.profile.tabprofile

import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment

/**
 * Created by lucas on 16/11/2018.
 */
 
class TabProfileFragment : BaseFragment(){

    companion object {

        fun newInstance(): TabProfileFragment {
            return TabProfileFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_tab_profile
}