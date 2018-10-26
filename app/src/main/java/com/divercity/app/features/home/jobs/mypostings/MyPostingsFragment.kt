package com.divercity.app.features.home.jobs.mypostings

import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment

/**
 * Created by lucas on 25/10/2018.
 */


class MyPostingsFragment : BaseFragment() {

    lateinit var viewModel: MyPostingsViewModel

    companion object {

        fun newInstance(): MyPostingsFragment {
            return MyPostingsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_my_postings


}