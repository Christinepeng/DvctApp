package com.divercity.android.features.onboarding.profileprompt

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
                    txt_subtitle1.text = this.getString(R.string.profile_prompt_subtitle_recruiter, viewModel.userName)
                else
                    txt_subtitle1.text = this.getString(R.string.profile_prompt_subtitle_nonrecruiter, viewModel.userName)
            }
        })
    }

    private fun setupView() {
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