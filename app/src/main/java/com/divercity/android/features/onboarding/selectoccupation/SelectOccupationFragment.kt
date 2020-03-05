package com.divercity.android.features.onboarding.selectoccupation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import javax.inject.Inject

class SelectOccupationFragment : BaseFragment() {

    lateinit var viewModel: SelectOccupationViewModel

    @Inject
    lateinit var adapter: SelectOccupationAdapter

    companion object {
        fun newInstance() = SelectOccupationFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_onboarding_header_search_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectOccupationViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_continue.visibility = View.VISIBLE
        include_search.visibility = View.GONE

        list.adapter = adapter
        setupHeader()
        setupEvents()
        subscribeToLiveData()
    }

    private fun setupEvents(){

        btn_continue.setOnClickListener {
            val listStr = adapter.listSelected.toString()
            viewModel.updateUserProfile(listStr.substring(1, listStr.length - 1))
        }
    }

    private fun setupHeader() {

        include_header.apply {

            txt_title.setText(R.string.select_your_occupation)

            btn_close.setOnClickListener {
                navigator.navigateToProfessionalInfoActivity(requireActivity())
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
                    navigator.navigateToProfessionalInfoActivity(requireActivity())
                }
            }
        })
    }
}