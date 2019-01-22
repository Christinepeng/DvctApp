package com.divercity.android.features.ethnicity.onboarding


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.company.base.adapter.CompanyAdapter
import com.divercity.android.features.ethnicity.base.Ethnicity
import com.divercity.android.features.ethnicity.base.SelectEthnicityFragment
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import javax.inject.Inject

class OnboardingEthnicityFragment : BaseFragment(), SelectEthnicityFragment.Listener {

    lateinit var viewModel: OnboardingEthnicityViewModel

    @Inject
    lateinit var adapter: CompanyAdapter

    var currentProgress: Int = 0

    companion object {
        private const val PARAM_PROGRESS = "paramProgress"

        fun newInstance(progress: Int): OnboardingEthnicityFragment {
            val fragment = OnboardingEthnicityFragment()
            val arguments = Bundle()
            arguments.putInt(PARAM_PROGRESS, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_toolbar_onboarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[OnboardingEthnicityViewModel::class.java]
        currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().add(
                R.id.fragment_fragment_container, SelectEthnicityFragment.newInstance()).commit()
        setupHeader()
        subscribeToLiveData()
    }

    private fun setupHeader() {
        include_header.apply {

            progress_bar.apply {
                max = 100
                progress = 0
                setProgressWithAnim(currentProgress)
            }
            txt_title.setText(R.string.select_your_ethnicity)

            txt_progress.text = currentProgress.toString().plus("%")

            btn_close.setOnClickListener {
                navigator.navigateToHomeActivity(activity!!)
            }

            btn_skip.setOnClickListener {
                navigator.navigateToNextOnboarding(activity!!,
                        viewModel.getAccountType(),
                        currentProgress,
                        false
                )
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.updateUserProfileResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    navigator.navigateToNextOnboarding(activity!!,
                            viewModel.getAccountType(),
                            currentProgress,
                            true
                    )
                }
            }
        })
    }

    override fun onEthnicityChosen(ethnicity: Ethnicity) {
        viewModel.updateUserProfile(getString(ethnicity.textId))
    }
}
