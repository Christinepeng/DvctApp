package com.divercity.android.features.agerange.onboarding

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.agerange.base.SelectAgeFragment
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import javax.inject.Inject

class OnboardingAgeFragment : BaseFragment(), SelectAgeFragment.Listener {

    lateinit var viewModel: OnboardingAgeViewModel

    @Inject
    lateinit var adapter: OnboardingAgeViewModel

    companion object {
        fun newInstance() = OnboardingAgeFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_toolbar_onboarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[OnboardingAgeViewModel::class.java]
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

            txt_title.setText(R.string.select_your_agerange)

            btn_close.setOnClickListener {
                navigator.navigateToPersonalInfoActivity(requireActivity())
            }
        }
    }

    private fun navigateToNext(shouldIncrement: Boolean) {
        navigator.navigateToPersonalInfoActivity(requireActivity())
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
                    navigator.navigateToPersonalInfoActivity(requireActivity())
//                    navigateToNext(true)
                }
            }
        })
    }

    override fun onAgeRangeChosen(ageRange: String) {
        viewModel.ageRangeSelected = ageRange
    }
}
