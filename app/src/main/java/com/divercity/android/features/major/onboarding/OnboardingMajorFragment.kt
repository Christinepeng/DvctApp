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

    companion object {
        fun newInstance() = OnboardingMajorFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_toolbar_onboarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[OnboardingMajorViewModel::class.java]
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

            txt_title.setText(R.string.select_your_school)

            btn_close.setOnClickListener {
                navigator.navigateToProfessionalInfoActivity(requireActivity())
            }

//            btn_skip.setOnClickListener {
//                navigator.navigateToProfessionalInfoActivity(requireActivity())
//            }
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
//                    navigator.navigateToProfessionalInfoActivity(requireActivity())
                    navigator.navigateToNextOnboarding(requireActivity(),
                        viewModel.getAccountType(),
                        0,
                        true
                    )
                }
            }
        })
    }

    override fun onMajorChosen(major: Major) {
//        viewModel.updateUserProfile(major)
//        navigator.navigateToProfessionalInfoActivity(requireActivity())
        navigator.navigateToNextOnboarding(
            requireActivity(),
            viewModel.getAccountType(),
            0,
            true
        )
    }
}
