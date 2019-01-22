package com.divercity.android.features.onboarding.profileprompt

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[ProfilePromptViewModel::class.java]
        subscribeToLiveData()
        setupView()
    }

    private fun subscribeToLiveData() {
        viewModel.showRecruiterText.observe(this, Observer {
            it?.let { data ->
                if (data)
                    txt_subtitle.text = this.getString(R.string.profile_prompt_subtitle_recruiter)
                else
                    txt_subtitle.text = this.getString(R.string.profile_prompt_subtitle_nonrecruiter)
            }
        })
    }

    private fun setupView() {
        btn_next.setOnClickListener {
            navigator.navigateToNextOnboarding(activity!!,
                    viewModel.accountType!!,
                    0,
                    false
            )
        }

        btn_not_now.setOnClickListener {
            navigator.navigateToHomeActivity(activity!!)
        }
    }


}