package com.divercity.app.features.agerange.onboarding

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.Status
import com.divercity.app.features.agerange.base.SelectAgeFragment
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import javax.inject.Inject

class OnboardingAgeFragment : BaseFragment(), SelectAgeFragment.Listener {

    lateinit var viewModel: OnboardingAgeViewModel

    @Inject
    lateinit var adapter: OnboardingAgeViewModel

    var currentProgress: Int = 0

    companion object {
        private const val PARAM_PROGRESS = "paramProgress"

        fun newInstance(progress: Int): OnboardingAgeFragment {
            val fragment = OnboardingAgeFragment()
            val arguments = Bundle()
            arguments.putInt(PARAM_PROGRESS, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_toolbar_onboarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[OnboardingAgeViewModel::class.java]
        currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().add(
                R.id.fragment_fragment_container, SelectAgeFragment.newInstance()).commit()
        setupHeader()
        setupView()
        subscribeToLiveData()
    }

    private fun setupView() {
        btn_continue.visibility = View.VISIBLE
        btn_continue.setOnClickListener {
            if (viewModel.ageRangeSelected != null) {
                viewModel.updateUserProfileWithSelectedAgeRange()
            } else
                navigateToNext(false)
        }
    }

    private fun setupHeader() {
        include_header.apply {
            currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0

            progress_bar.apply {
                max = 100
                progress = 0
                setProgressWithAnim(currentProgress)
            }
            txt_title.setText(R.string.select_your_agerange)

            txt_progress.text = currentProgress.toString().plus("%")

            btn_close.setOnClickListener {
                navigator.navigateToHomeActivity(activity!!)
            }

            btn_skip.setOnClickListener {
                navigateToNext(false)
            }
        }
    }

    private fun navigateToNext(shouldIncrement: Boolean) {
        navigator.navigateToNextOnboarding(activity!!,
                viewModel.getAccountType(),
                currentProgress,
                shouldIncrement
        )
    }

    private fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun subscribeToLiveData() {
        viewModel.updateUserProfileResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    showToast(response.message ?: "Error")
                }
                Status.SUCCESS -> {
                    hideProgress()
                    navigateToNext(true)
                }
            }
        })
    }

    override fun onAgeRangeChosen(ageRange: String) {
        viewModel.ageRangeSelected = ageRange
    }
}
