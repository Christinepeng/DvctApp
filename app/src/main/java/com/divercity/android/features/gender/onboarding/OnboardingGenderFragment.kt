package com.divercity.android.features.gender.onboarding


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.company.selectcompany.base.adapter.CompanyAdapter
import com.divercity.android.features.gender.base.SelectGenderFragment
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import javax.inject.Inject

class OnboardingGenderFragment : BaseFragment(), SelectGenderFragment.Listener {

    lateinit var viewModel: OnboardingGenderViewModel

    @Inject
    lateinit var adapter: CompanyAdapter


    companion object {
        fun newInstance() = OnboardingGenderFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_toolbar_onboarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[OnboardingGenderViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().add(
                R.id.fragment_fragment_container, SelectGenderFragment.newInstance()).commit()
        setupHeader()
        subscribeToLiveData()
    }

    private fun setupHeader() {
        include_header.apply {
            txt_title.setText(R.string.select_your_gender)

            btn_close.setOnClickListener {
                navigator.navigateToPersonalInfoActivity(requireActivity())
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
                    navigator.navigateToPersonalInfoActivity(requireActivity())
                }
            }
        })
    }

    override fun onGenderChosen(gender: String) {
        viewModel.updateUserProfile(gender)
    }
}
