package com.divercity.android.features.company.ratecompany

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.Status
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.data.entity.company.review.CompanyDiversityReviewEntityResponse
import com.divercity.android.features.dialogs.WriteReviewCompanyDialogFragment
import kotlinx.android.synthetic.main.fragment_rate_company.*
import kotlinx.android.synthetic.main.view_rate_company.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 16/11/2018.
 */

class RateCompanyFragment : BaseFragment() {

    lateinit var viewModel: RateCompanyViewModel

    companion object {
        private const val PARAM_IS_EDITION = "paramIsEdition"

        fun newInstance(isEdition: Boolean): RateCompanyFragment {
            val fragment = RateCompanyFragment()
            val arguments = Bundle()
            arguments.putBoolean(PARAM_IS_EDITION, isEdition)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_rate_company

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(RateCompanyViewModel::class.java)

        if(DataHolder.hasData()){
            viewModel.setCompany(DataHolder.data!!)
        } else {
            showToast("Error")
            requireActivity().finish()
        }

        initView()
        subscribeToLiveData()
    }

    private fun initView() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.rate_company_diversity)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        if (arguments?.getBoolean(PARAM_IS_EDITION) == true) {
            viewModel.fetchReview()
            btn_submit.setText(R.string.edit)
            btn_submit.setOnClickListener {
                if (et_review.text.toString().isEmpty())
                    showWriteReviewCompanyDialogFragment()
                else
                    editReview()
            }
        } else {
            btn_submit.setText(R.string.submit)
            btn_submit.setOnClickListener {
                if (et_review.text.toString().isEmpty())
                    showWriteReviewCompanyDialogFragment()
                else
                    rateCompany()
            }
        }
    }

    private fun editReview() {
        viewModel.updateReview(
            getRatingValue(rating_bar_gender),
            getRatingValue(rating_bar_race),
            getRatingValue(rating_bar_age),
            getRatingValue(rating_bar_sex_orien),
            getRatingValue(rating_bar_abel_bodiedness),
            et_review.text.toString()
        )
    }

    private fun rateCompany() {
        viewModel.rateCompany(
            getRatingValue(rating_bar_gender),
            getRatingValue(rating_bar_race),
            getRatingValue(rating_bar_age),
            getRatingValue(rating_bar_sex_orien),
            getRatingValue(rating_bar_abel_bodiedness),
            et_review.text.toString()
        )
    }

    private fun getRatingValue(ratingBar: RatingBar): Int = ratingBar.rating.toInt()

    private fun showCompanyData(company: CompanyResponse?) {
        company?.also {

            root_layout.visibility = View.VISIBLE

            GlideApp.with(this)
                .load(it.attributes?.photos?.medium)
                .into(img)

            txt_company_name.text = it.attributes?.name
        }
    }

    private fun showRateData(review: CompanyDiversityReviewEntityResponse) {
        rating_bar_gender.rating = review.attributes.genderRate?.toFloat() ?: 0f
        rating_bar_age.rating = review.attributes.ageRate?.toFloat() ?: 0f
        rating_bar_race.rating = review.attributes.raceEthnicityRate?.toFloat() ?: 0f
        rating_bar_abel_bodiedness.rating = review.attributes.ableBodiednessRate?.toFloat() ?: 0f
        rating_bar_sex_orien.rating =
            review.attributes.sexualOrientationRate?.toFloat() ?: 0f
        et_review.setText(review.attributes.review)
    }

    private fun subscribeToLiveData() {
        viewModel.companyLiveData.observe(viewLifecycleOwner, Observer { company ->
            showCompanyData(company)
        })

        viewModel.rateCompanyResponse.observe(this, Observer { response ->
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
                    requireActivity().apply {
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            }
        })

        viewModel.fetchReviewResponse.observe(this, Observer { response ->
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
                    showRateData(response.data!!)
                }
            }
        })
    }

    private fun showWriteReviewCompanyDialogFragment() {
        val dialog = WriteReviewCompanyDialogFragment.newInstance()
        dialog.listener = object : WriteReviewCompanyDialogFragment.Listener {
            override fun onSubmit() {
                rateCompany()
            }
        }
        dialog.show(childFragmentManager, null)
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    enum class DataHolder {
        INSTANCE;

        private var company: CompanyResponse? = null

        companion object {

            fun hasData(): Boolean {
                return INSTANCE.company != null
            }

            var data: CompanyResponse?
                get() {
                    val company = INSTANCE.company
                    INSTANCE.company = null
                    return company
                }
                set(objectList) {
                    INSTANCE.company = objectList
                }
        }
    }
}