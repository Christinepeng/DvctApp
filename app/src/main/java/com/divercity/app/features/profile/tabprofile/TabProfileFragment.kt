package com.divercity.app.features.profile.tabprofile

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.Status
import com.divercity.app.data.entity.location.LocationResponse
import com.divercity.app.data.entity.user.response.UserResponse
import com.divercity.app.features.agerange.withtoolbar.ToolbarAgeFragment
import com.divercity.app.features.ethnicity.withtoolbar.ToolbarEthnicityFragment
import com.divercity.app.features.gender.withtoolbar.ToolbarGenderFragment
import com.divercity.app.features.location.withtoolbar.ToolbarLocationFragment
import kotlinx.android.synthetic.main.fragment_tab_profile.*

/**
 * Created by lucas on 16/11/2018.
 */

class TabProfileFragment : BaseFragment() {

    private lateinit var viewModel: TabProfileViewModel

    companion object {

        const val REQUEST_CODE_ETHNICITY = 100
        const val REQUEST_CODE_GENDER = 150
        const val REQUEST_CODE_AGERANGE = 180
        const val REQUEST_CODE_LOCATION = 200

        fun newInstance(): TabProfileFragment {
            return TabProfileFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_tab_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(TabProfileViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeToLiveData()
    }

    fun setupView() {

        lay_ethnicity.setOnClickListener {
            navigator.navigateToToolbarEthnicityActivityForResult(this, REQUEST_CODE_ETHNICITY)
        }

        lay_gender.setOnClickListener {
            navigator.navigateToToolbarGenderActivityForResult(this, REQUEST_CODE_GENDER)
        }

        lay_age_range.setOnClickListener {
            navigator.navigateToToolbarAgeActivityForResult(this, REQUEST_CODE_AGERANGE)
        }

        lay_location.setOnClickListener {
            navigator.navigateToToolbarLocationActivityForResult(this, REQUEST_CODE_LOCATION)
        }

        txt_ethnicity.text = viewModel.getEthnicity()
        txt_gender.text = viewModel.getGender()
        txt_industry.text = viewModel.getIndustries()
        txt_age_range.text = viewModel.getAgeRange()
        txt_location.text = viewModel.getLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_ETHNICITY) {
                val ethnicity = data?.extras?.getString(ToolbarEthnicityFragment.ETHNICITY_PICKED)
                viewModel.updateEthnicity(ethnicity)
            } else if (requestCode == REQUEST_CODE_GENDER) {
                val gender = data?.extras?.getString(ToolbarGenderFragment.GENDER_PICKED)
                viewModel.updateGender(gender)
            } else if (requestCode == REQUEST_CODE_AGERANGE) {
                val ageRange = data?.extras?.getString(ToolbarAgeFragment.AGE_RANGE_PICKED)
                ageRange?.apply {
                    viewModel.updateAgeRange(this)
                }
            } else if (requestCode == REQUEST_CODE_LOCATION) {
                val location = data?.extras?.getParcelable<LocationResponse>(ToolbarLocationFragment.LOCATION_PICKED)
                location?.apply {
                    viewModel.updateLocation(location)
                }
            }
        }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun subscribeToLiveData() {
        viewModel.updateUserProfileResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    showToast(response.message)
                }

                Status.SUCCESS -> {
                    hideProgress()
                    setData(response.data)
                }
            }
        })
    }

    private fun setData(user: UserResponse?) {

        txt_ethnicity.text = user?.attributes?.ethnicity
        txt_gender.text = user?.attributes?.gender
        txt_age_range.text = user?.attributes?.ageRange
        txt_location.text = user?.attributes?.city.plus(", ").plus(user?.attributes?.country)
    }
}