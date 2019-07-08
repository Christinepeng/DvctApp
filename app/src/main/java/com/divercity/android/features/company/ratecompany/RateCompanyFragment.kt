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
import kotlinx.android.synthetic.main.fragment_rate_company.*
import kotlinx.android.synthetic.main.view_rate_company.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 16/11/2018.
 */

class RateCompanyFragment : BaseFragment() {

    lateinit var viewModel: RateCompanyViewModel

    lateinit var companyId: String

    companion object {
        private const val PARAM_COMPANY_ID = "paramCompanyId"

        fun newInstance(companyId: String): RateCompanyFragment {
            val fragment = RateCompanyFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_COMPANY_ID, companyId)
            fragment.arguments = arguments
            return fragment
        }
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

    override fun layoutId(): Int = R.layout.fragment_rate_company

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        companyId = arguments?.getString(PARAM_COMPANY_ID)!!
        if (DataHolder.hasData()) {
            viewModel.companyLiveData.postValue(DataHolder.data)
        } else {
            showToast("Error")
            requireActivity().finish()
        }
        initView()
        subscribeToLiveData()
        setupToolbar()
    }

    private fun initViewModel() {
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(RateCompanyViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.rate_company_diversity)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun initView() {
        btn_submit.setOnClickListener {
            viewModel.rateCompany(
                getRatingValue(rating_bar_gender),
                getRatingValue(rating_bar_race),
                getRatingValue(rating_bar_age),
                getRatingValue(rating_bar_sex_orien),
                getRatingValue(rating_bar_abel_bodiedness),
                et_review.text.toString())
        }
    }

    private fun getRatingValue(ratingBar: RatingBar) : Int = ratingBar.rating.toInt()

    private fun showData(company: CompanyResponse?) {
        company?.also {

            root_layout.visibility = View.VISIBLE

            GlideApp.with(this)
                .load(it.attributes?.photos?.medium)
                .into(img)

            txt_company_name.text = it.attributes?.name
        }
    }

    private fun subscribeToLiveData() {
        viewModel.companyLiveData.observe(viewLifecycleOwner, Observer { company ->
            showData(company)
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
    }

    private fun showToast(resId: Int) {
        Toast.makeText(context!!, resId, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(message: String?) {
        Toast.makeText(context!!, message, Toast.LENGTH_SHORT).show()
    }
}