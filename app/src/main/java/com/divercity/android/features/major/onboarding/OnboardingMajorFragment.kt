package com.divercity.android.features.major.onboarding


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.company.selectcompany.base.adapter.CompanyAdapter
import com.divercity.android.features.major.base.SelectMajorFragment
import com.divercity.android.features.school.base.SelectSchoolFragment
import com.divercity.android.model.Major
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import javax.inject.Inject

class OnboardingMajorFragment : BaseFragment(), SelectMajorFragment.Listener {

    lateinit var viewModel: OnboardingMajorViewModel

    @Inject
    lateinit var adapter: CompanyAdapter

    var currentProgress: Int = 0

    companion object {
        private const val PARAM_PROGRESS = "paramProgress"

        fun newInstance(progress: Int): OnboardingMajorFragment {
            val fragment = OnboardingMajorFragment()
            val arguments = Bundle()
            arguments.putInt(PARAM_PROGRESS, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_toolbar_onboarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[OnboardingMajorViewModel::class.java]
        currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().add(
            R.id.fragment_fragment_container, SelectSchoolFragment.newInstance()
        ).commit()
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
            txt_title.setText(R.string.select_your_school)

            txt_progress.text = currentProgress.toString().plus("%")

            btn_close.setOnClickListener {
                navigator.navigateToHomeActivity(requireActivity())
            }

            btn_skip.setOnClickListener {
                navigator.navigateToNextOnboarding(requireActivity(),
                    viewModel.getAccountType(),
                    currentProgress,
                    false
                )
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.updateUserProfileResponse.observe(this, Observer { school ->
            when (school?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, school.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    navigator.navigateToNextOnboarding(requireActivity(),
                        viewModel.getAccountType(),
                        currentProgress,
                        true
                    )
                }
            }
        })
    }

    override fun onMajorChosen(major: Major) {
//        viewModel.updateUserProfile(major)
        navigator.navigateToNextOnboarding(
            requireActivity(),
            viewModel.getAccountType(),
            currentProgress,
            true
        )
    }
}
