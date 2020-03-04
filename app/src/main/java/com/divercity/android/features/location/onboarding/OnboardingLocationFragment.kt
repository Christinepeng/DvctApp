package com.divercity.android.features.location.onboarding

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.data.entity.location.LocationResponse
import com.divercity.android.features.company.selectcompany.base.adapter.CompanyAdapter
import com.divercity.android.features.location.base.SelectLocationFragment
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import javax.inject.Inject

class OnboardingLocationFragment : BaseFragment(), SelectLocationFragment.Listener {

    lateinit var viewModel: OnboardingLocationViewModel

    @Inject
    lateinit var adapter: CompanyAdapter

    companion object {
        fun newInstance() = OnboardingLocationFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_toolbar_onboarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[OnboardingLocationViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().add(
                R.id.fragment_fragment_container, SelectLocationFragment.newInstance()).commit()
        setupHeader()
        subscribeToLiveData()
    }

    private fun setupHeader() {
        include_header.apply {

            txt_title.setText(R.string.select_your_location)

            btn_close.setOnClickListener {
                navigator.navigateToPersonalInfoActivity(requireActivity())
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.updateUserProfileResponse.observe(viewLifecycleOwner, Observer { response ->
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

    override fun onLocationChoosen(location: LocationResponse) {
        viewModel.updateUserProfile(location)
    }
}
