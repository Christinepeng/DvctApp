package com.divercity.app.features.onboarding.profileprompt

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile_prompt.*

/**
 * Created by lucas on 26/10/2018.
 */

class ProfilePromptFragment : BaseFragment() {

    lateinit var viewModel: ProfilePromptViewModel

    companion object {

        fun newInstance(): ProfilePromptFragment = ProfilePromptFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_profile_prompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[ProfilePromptViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_next.setOnClickListener {
            navigator.navigateToNextOnboarding(activity!!,
                    viewModel.accountType,
                    0,
                    false
            )
        }

        btn_not_now.setOnClickListener {
            navigator.navigateToHomeActivity(activity!!)
        }
    }

}